package com.javarush.jira.profile.internal.web;

import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.profile.*;
import com.javarush.jira.profile.internal.*;
import com.javarush.jira.profile.internal.model.*;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.Set;
import java.util.stream.Collectors;
import static com.javarush.jira.common.util.JsonUtil.writeValue;
import static com.javarush.jira.login.internal.web.UserTestData.ADMIN_MAIL;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



class ProfileRestControllerTest extends AbstractControllerTest {
    private static final String PROFILE_ENDPOINT = ProfileRestController.REST_URL;
    private final int ADMIN_ID = 2;
    private final ProfileTo testProfileForUpdate = new ProfileTo(2L, null, Set.of(new ContactTo("phone", "1111")));

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileMapper profileMapper;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void testFetchProfileByUser() throws Exception {
        perform(MockMvcRequestBuilders.get(PROFILE_ENDPOINT))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(ADMIN_ID)));
    }

    @Test
    void testUnauthenticatedAccess() throws Exception {
        perform(MockMvcRequestBuilders.get(PROFILE_ENDPOINT))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void testUpdateProfile() throws Exception {
        perform(MockMvcRequestBuilders.put(PROFILE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(testProfileForUpdate)))
                .andExpect(status().is2xxSuccessful());

        Set<String> actualContacts = profileRepository.getExisted(ADMIN_ID).getContacts().stream()
                .map(Contact::getValue)
                .collect(Collectors.toSet());

        Set<String> expectedContacts = testProfileForUpdate.getContacts()
                .stream()
                .map(profileMapper::toContact)
                .map(Contact::getValue)
                .collect(Collectors.toSet());

        assertTrue(CollectionUtils.isEqualCollection(expectedContacts, actualContacts));
    }
}
