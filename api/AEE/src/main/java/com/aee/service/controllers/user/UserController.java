package com.aee.service.controllers.user;

import com.aee.service.controllers.base.BaseController;
import com.aee.service.mapper.UniversityMapper;
import com.aee.service.models.university.Field;
import com.aee.service.models.university.University;
import com.aee.service.payload.response.*;
import com.aee.service.repository.university.FieldRepository;
import com.aee.service.repository.university.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

import static com.aee.service.repository.university.UniversitySpecification.hasName;
import static com.aee.service.repository.university.UniversitySpecification.inZone;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/u")
public class UserController extends BaseController {

    @Autowired
    UniversityRepository universityRepository;

    @Autowired
    UniversityMapper universityMapper;

    @Autowired
    FieldRepository fieldRepository;



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
    public BasePagingResponse<List<UniversityResponse>> listUniversity(@Valid @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "3") int size) {
        BasePagingResponse<List<UniversityResponse>>baseResponse = new BasePagingResponse<>();
        Pageable paging = PageRequest.of(page, size);
        Page<University> universities = universityRepository.findAll(paging);
        baseResponse.setData(universityMapper.fromUniversityListToUniversityListResponse(universities.getContent()));
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
    @GetMapping(value = "/list-field/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<List<FieldResponse>> getField(@PathVariable long id) {
        BaseResponse<List<FieldResponse>> baseResponse = new BaseResponse<>();
        List<Field> fieldResponses = fieldRepository.findAllByUniversityId(id).orElse(null);
        if (fieldResponses == null){
            baseResponse.setData(Collections.emptyList());
        } else {
            baseResponse.setData(universityMapper.fromFieldListToFieldListResponse(fieldResponses));
        }
        baseResponse.setResult(true);
        return baseResponse;
    }
}
