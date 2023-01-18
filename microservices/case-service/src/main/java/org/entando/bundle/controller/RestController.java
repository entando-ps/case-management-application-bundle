package org.entando.bundle.controller;

import org.entando.bundle.entity.Process;
import org.entando.bundle.service.impl.FileServiceImpl;
import org.entando.bundle.service.ProcessService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.List;
import java.util.Optional;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RestController {

    private final ProcessService processService;

    private final FileServiceImpl fileService;

    public RestController(ProcessService processService, FileServiceImpl fileService) {
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

    @PostMapping("/start")
    public @ResponseBody Process startProcess(@RequestParam(value = "attachment") MultipartFile[] files,
                                              @RequestParam(value = "name", required = false) String name) {

        for (MultipartFile file : files) {
            System.out.println("NOME: " + file.getName());
            System.out.println("size: " + file.getSize());
            System.out.println("content type: " + file.getContentType());
            System.out.println("original filename: " + file.getOriginalFilename());
            System.out.println("");
        }
        System.out.println("\nData: " + name);
        return null;
    }

}