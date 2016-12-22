package com.crossover.trial.journals.controller;

import com.crossover.trial.journals.actor.AkkaHelper;
import com.crossover.trial.journals.actor.message.NewJournalAddedMessage;
import com.crossover.trial.journals.actor.notifier.NewJournalNotifierActor;
import com.crossover.trial.journals.model.*;
import com.crossover.trial.journals.repository.PublisherRepository;
import com.crossover.trial.journals.service.CurrentUser;
import com.crossover.trial.journals.service.JournalService;
import com.crossover.trial.journals.service.SubscriptionService;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.DateUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.emptyList;
import static org.codehaus.groovy.runtime.InvokerHelper.asList;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.thymeleaf.util.DateUtils.createNow;

/**
 * Created by YongSengChia on 12/22/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class PublisherControllerTest {

    @InjectMocks
    PublisherController publisherController;

    @Mock
    PublisherRepository publisherRepository;

    @Mock
    JournalService journalService;

    @Mock
    SubscriptionService subscriptionService;

    @Mock
    AkkaHelper akkaHelper;

    @Test
    public void provideUploadInfo() throws Exception {
        Model model = mock(Model.class);
        String expectedInfo = "publisher/publish";
        String providedInfo = publisherController.provideUploadInfo(model);

        assertEquals(expectedInfo, providedInfo);
    }

    @Test
    public void handleFileUpload() throws Exception {
        TestingAuthenticationToken principal = mock(TestingAuthenticationToken.class);
        MultipartFile file = mock(MultipartFile.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        User user = new User(1L, "user1", "password1", Boolean.TRUE, Role.PUBLISHER, emptyList());
        CurrentUser currentUser = new CurrentUser(user);
        Publisher publisher = new Publisher(1L, user, "publisher1");
        Category category = new Category(1L, "category1");
        Journal journal = new Journal(1L, "journal1", createNow().getTime(), publisher, "some-uuid", category);
        List akkaMessages = asList(new NewJournalAddedMessage(journal, asList(user)));

        when(file.isEmpty()).thenReturn(false);
        when(file.getInputStream()).thenReturn(new ByteInputStream());
        when(principal.getPrincipal()).thenReturn(currentUser);
        when(publisherRepository.findByUser(user)).thenReturn(Optional.ofNullable(publisher));
        when(journalService.publish(eq(publisher), any(Journal.class), eq(category.getId()))).thenReturn(journal);
        when(subscriptionService.findSubscribersByCategory(category.getId())).thenReturn(asList(user));
        doNothing().when(akkaHelper).tell(NewJournalNotifierActor.NAME, akkaMessages);

        publisherController.handleFileUpload("fileName", 1L, file, redirectAttributes, principal);

        verify(journalService, times(1)).publish(eq(publisher), any(Journal.class), eq(category.getId()));
        verify(subscriptionService, times(1)).findSubscribersByCategory(category.getId());
        verify(akkaHelper, times(1)).tell(NewJournalNotifierActor.NAME, akkaMessages);
        verifyNoMoreInteractions(journalService, subscriptionService, akkaHelper);
    }

    @Test
    public void getFileName() throws Exception {
        Long publisherId = 1L;
        String expectedUuid = UUID.randomUUID().toString();
        String retrievedFileName = PublisherController.getFileName(publisherId, expectedUuid);

        assertTrue(retrievedFileName.contains(expectedUuid));
        assertTrue(retrievedFileName.endsWith(expectedUuid + ".pdf"));
    }

    @Test
    public void getDirectory() throws Exception {
        Long expectedPublisherId = 1L;
        String retrievedDirectory = PublisherController.getDirectory(expectedPublisherId);

        assertTrue(retrievedDirectory.endsWith(String.valueOf(expectedPublisherId)));
        assertTrue(retrievedDirectory.endsWith("upload" + File.separator + String.valueOf(expectedPublisherId)));
    }

}