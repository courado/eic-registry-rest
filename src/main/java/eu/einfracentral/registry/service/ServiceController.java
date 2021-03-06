package eu.einfracentral.registry.service;

import eu.einfracentral.domain.Service;
import eu.openminted.registry.core.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

/**
 * Created by pgl on 4/7/2017.
 */
@RestController
@RequestMapping("path")
public class ServiceController {
    @Autowired
    ServiceService serviceService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ResponseEntity<Service> getService(@PathVariable("id") String id) {
        String id_decoded = new String(Base64.getDecoder().decode(id));
        Service service = serviceService.get(id_decoded);
        if (service == null) {
            throw new ResourceNotFoundException();
        } else {
            return new ResponseEntity<>(service, HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json; charset=utf-8")
    public ResponseEntity<String> addService(@RequestBody Service service) {
        serviceService.add(service);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/xml")
    public ResponseEntity<String> addServiceXml(@RequestBody Service service) {
        serviceService.add(service);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateService(@RequestBody Service service) {
        serviceService.update(service);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteService(@RequestBody Service service) {
        serviceService.delete(service);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public ResponseEntity<String> uploadService(@RequestParam("filename") String filename, @RequestParam("file") MultipartFile file) {
        try {
            return new ResponseEntity<>(serviceService.uploadService(filename, file.getInputStream()), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
