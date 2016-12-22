package com.crossover.trial.journals.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import com.crossover.trial.journals.actor.AkkaHelper;
import com.crossover.trial.journals.actor.message.NewJournalAddedMessage;
import com.crossover.trial.journals.actor.notifier.NewJournalNotifierActor;
import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.model.User;
import com.crossover.trial.journals.service.JournalService;
import com.crossover.trial.journals.service.SubscriptionService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.crossover.trial.journals.Application;
import com.crossover.trial.journals.model.Publisher;
import com.crossover.trial.journals.repository.PublisherRepository;
import com.crossover.trial.journals.service.CurrentUser;

import static java.util.Arrays.asList;

@Controller
public class PublisherController {

    private final static Logger log = Logger.getLogger(PublisherController.class);

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private JournalService journalService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    AkkaHelper akkaHelper;

    @RequestMapping(method = RequestMethod.GET, value = "/publisher/publish")
    public String provideUploadInfo(Model model) {
        return "publisher/publish";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/publisher/publish")
    @PreAuthorize("hasRole('PUBLISHER')")
    public String handleFileUpload(@RequestParam("name") String name, @RequestParam("category") Long categoryId, @RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes, @AuthenticationPrincipal Principal principal) {

        CurrentUser activeUser = (CurrentUser) ((Authentication) principal).getPrincipal();
        Optional<Publisher> publisher = publisherRepository.findByUser(activeUser.getUser());

        String uuid = UUID.randomUUID().toString();
        File dir = new File(getDirectory(publisher.get().getId()));
        createDirectoryIfNotExist(dir);

        File f = new File(getFileName(publisher.get().getId(), uuid));
        if (!file.isEmpty()) {
            try {
                savedUploadedFile(file, f);

                Journal journal = new Journal();
                journal.setUuid(uuid);
                journal.setName(name);

                Journal publishedJournal =
                        journalService.publish(publisher.get(), journal, categoryId);

                if (publishedJournal != null) {
                    Collection<User> subcribers = subscriptionService.findSubscribersByCategory(categoryId);
                    // send newly publish journal notification
                    akkaHelper.tell(NewJournalNotifierActor.NAME, asList(
                            new NewJournalAddedMessage(publishedJournal, subcribers)));
                }
                return "redirect:/publisher/browse";
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("message",
                        "You failed to publish " + name + " => " + e.getMessage());
            }
        } else {
            redirectAttributes.addFlashAttribute("message",
                    "You failed to upload " + name + " because the file was empty");
        }

        return "redirect:/publisher/publish";
    }

    protected void savedUploadedFile(@RequestParam("file") MultipartFile file, File f) throws IOException {
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(f));
        FileCopyUtils.copy(file.getInputStream(), stream);
        stream.close();
    }

    protected boolean createDirectoryIfNotExist(File dir) {
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                return false;
            }
        }
        return true;
    }

    public static String getFileName(long publisherId, String uuid) {
        return getDirectory(publisherId) + "/" + uuid + ".pdf";
    }

    public static String getDirectory(long publisherId) {
        return Application.ROOT + File.separator + publisherId;
    }

}