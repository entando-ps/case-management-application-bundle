package org.entando.bundle.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.entando.bundle.domain.AuthorizedUser;
import org.entando.bundle.domain.CaseUserData;
import org.entando.bundle.domain.Delegation;
import org.entando.bundle.domain.SubscribedUser;
import org.entando.bundle.entity.Process;
import org.entando.bundle.service.CaseService;
import org.entando.bundle.service.FileService;
import org.entando.bundle.service.impl.CaseServiceImpl;
import org.entando.bundle.service.impl.FileServiceImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CaseController {

    private final CaseService processService;

    private final FileService fileService;

    public CaseController(CaseServiceImpl processService, FileServiceImpl fileService) {
        this.processService = processService;
        this.fileService = fileService;
    }

    @GetMapping("/processes")
    @PreAuthorize("hasAnyAuthority('case-management-admin')")
    public @ResponseBody List<Process> getProcesses() {
        return processService.getAllProcesses();
    }

    @GetMapping("/process/{id}")
    @PreAuthorize("hasAnyAuthority('case-management-admin')")
    public @ResponseBody Optional<Process> getProcess(@PathVariable Long id) {
        Optional<Process> process = processService.getProcess(id);
        return process;
    }

    @PostMapping("/processes")
    @PreAuthorize("hasAnyAuthority('case-management-entry')")
    public @ResponseBody Process createProcess(@RequestBody Process process) {
        return processService.crateProcess(process);
    }

    @PostMapping(value = "/start", consumes = {"multipart/form-data"})
    public @ResponseBody Process startProcess(@RequestPart("attachments") MultipartFile[] files,
                                              @Valid @RequestPart("case_metadata") CaseUserData data) {

        try {
            for (MultipartFile file : files) {
                System.out.println("NOME: " + file.getName());
                System.out.println("size: " + file.getSize());
                System.out.println("content type: " + file.getContentType());
                System.out.println("original filename: " + file.getOriginalFilename());
                System.out.println("");
            }

            System.out.println(">>> " + data.getSubscriber().getName());
            System.out.println(">>> " + data.getSubscriber().getDelegation());

//            ObjectMapper mapper = new ObjectMapper();
//            mapper.registerModule(new JavaTimeModule());
//            CaseUserData user = mapper.readValue(data, CaseUserData.class);
//            System.out.println("\n>>> " + user.getSubscriber().getDelegation());
//            System.out.println("\n>>> " + user.getSubscriber().getName());

        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    @GetMapping("/case")
    public @ResponseBody CaseUserData getCase() {
        CaseUserData caseUserData = new CaseUserData();

        SubscribedUser subscriber = new SubscribedUser();

        subscriber.setName("Matteo");
        subscriber.setLastname("Emanuele");
        subscriber.setBirthDate(LocalDate.now());
        subscriber.setBirthCountry("Italy");
        subscriber.setBirthCity("Carbonia");
        subscriber.setBirthProvince("CA");
        subscriber.setBirthRegion("Sardegna");
        subscriber.setFiscalCode("MTTMML77G05U654U");
        subscriber.setEmail("mail@gmail.com");
        subscriber.setLandline("178161832");
        subscriber.setMobile("3281235123");
        subscriber.setSector("sector");
        subscriber.setDelegation(Delegation.TIPO_DUE);

        caseUserData.setSubscriber(subscriber);

        AuthorizedUser authorized = new AuthorizedUser();

        authorized.setName("Matteo");
        authorized.setLastname("Emanuele");
        authorized.setBirthDate(LocalDate.now());
        authorized.setBirthCountry("Italy");
        authorized.setBirthCity("Carbonia");
        authorized.setBirthProvince("CA");
        authorized.setBirthRegion("Sardegna");
        authorized.setFiscalCode("MTTMML77G05U654U");
        authorized.setEmail("mail@gmail.com");;
        authorized.setMobile("3281235123");
        authorized.setRole("sector");

        caseUserData.setAuthorized(authorized);

        return caseUserData;
    }

}