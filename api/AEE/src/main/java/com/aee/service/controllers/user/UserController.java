package com.aee.service.controllers.user;

import com.aee.service.controllers.base.BaseController;
import com.aee.service.models.university.University;
import com.aee.service.payload.response.BasePagingResponse;
import com.aee.service.payload.response.BaseResponse;
import com.aee.service.payload.response.ProfileResponse;
import com.aee.service.repository.university.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.aee.service.repository.university.UniversitySpecification.hasName;
import static com.aee.service.repository.university.UniversitySpecification.inZone;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/u")
public class UserController extends BaseController {

    @Autowired
    UniversityRepository universityRepository;


    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<ProfileResponse> profile() {
        BaseResponse<ProfileResponse> baseResponse = new BaseResponse<>();
        if (    getUserDetails() != null) {
            ProfileResponse response = new ProfileResponse();
            response.setFullName(getUserDetails().getUsername());
            response.setAvatarPath(getUserDetails().getAvatarPath());
            baseResponse.setData(response);
            baseResponse.setMessage("Profile success");
            baseResponse.setResult(true);
            return baseResponse;
        }
        return baseResponse;
    }

    @GetMapping(value = "/list-university", produces = MediaType.APPLICATION_JSON_VALUE)
    public BasePagingResponse<List<University>> listUniversity(@Valid @RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "3") int size) {
        BasePagingResponse<List<University>>baseResponse = new BasePagingResponse<>();
        Pageable paging = PageRequest.of(page, size);
        Page<University> universities = universityRepository.findAll(paging);
        baseResponse.setData(universities.getContent());
        baseResponse.setCurrentPage(universities.getNumber());
        baseResponse.setTotalPages(universities.getTotalPages());
        baseResponse.setTotalItems((int) universities.getTotalElements());
        baseResponse.setResult(true);
        return baseResponse;
    }
    @GetMapping(value = "/search-university", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<List<University>> searchUniversity(@Valid @RequestParam(name = "zone") String zone,
                                                               @RequestParam(name = "name") String name) {
        BaseResponse<List<University>> baseResponse = new BaseResponse<>();
        List<University> universities = null;
        if (name!=null){
            universities = universityRepository.findAll(hasName(name));
        } else if (zone!=null){
            universities = universityRepository.findAll(inZone(zone));
        }
        baseResponse.setData(universities);
        baseResponse.setResult(true);
        return baseResponse;
    }
}
