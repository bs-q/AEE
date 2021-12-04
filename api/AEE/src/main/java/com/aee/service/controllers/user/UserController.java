package com.aee.service.controllers.user;

import com.aee.service.controllers.base.BaseController;
import com.aee.service.mapper.ChatMapper;
import com.aee.service.mapper.NewsMapper;
import com.aee.service.mapper.UniversityMapper;
import com.aee.service.models.User;
import com.aee.service.models.news.News;
import com.aee.service.models.post.Post;
import com.aee.service.models.university.Field;
import com.aee.service.models.university.University;
import com.aee.service.payload.request.ChangePasswordRequest;
import com.aee.service.payload.response.*;
import com.aee.service.repository.news.NewsRepository;
import com.aee.service.repository.post.PostRepository;
import com.aee.service.repository.university.FieldRepository;
import com.aee.service.repository.university.UniversityRepository;
import com.aee.service.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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

    @Autowired
    UserRepository userRepository;


    @Autowired
    PostRepository postRepository;

    @Autowired
    ChatMapper chatMapper;

    @Autowired
    NewsMapper newsMapper;

    @Autowired
    NewsRepository newsRepository;

    @Autowired
    PasswordEncoder encoder;



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
    public BaseResponse<List<UniversityResponse>> searchMyUniversity(@RequestParam(name = "zone",required = false) String zone,
                                                               @RequestParam(name = "name") String name) {
        BaseResponse<List<UniversityResponse>> baseResponse = new BaseResponse<>();
        List<University> universities = null;
        if (name!=null){
            universities = universityRepository.findAllByNameContaining(name);
        } else if (zone!=null){
            universities = universityRepository.findAll(inZone(zone));
        }
        baseResponse.setData(universityMapper.fromUniversityListToUniversityListResponse(universities));
        baseResponse.setResult(true);
        return baseResponse;
    }
    @GetMapping(value = "/search-post", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<List<PostResponse>> searchPost(@RequestParam(name = "title") String name) {
        BaseResponse<List<PostResponse>> baseResponse = new BaseResponse<>();
        List<Post> posts = postRepository.findAllByTitleContaining(name);
        if (posts == null || posts.isEmpty()){
            baseResponse.setData(Collections.emptyList());
        } else {
            baseResponse.setData(chatMapper.fromPostListToPostResponseList(posts));
        }
        baseResponse.setResult(true);
        return baseResponse;
    }
    @GetMapping(value = "/my-post", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<List<PostResponse>> myPost() {
        BaseResponse<List<PostResponse>> baseResponse = new BaseResponse<>();
        List<Post> posts = postRepository.findAllByCreatorId(getUserDetails().getId());
        if (posts == null || posts.isEmpty()){
            baseResponse.setData(Collections.emptyList());
        } else {
            baseResponse.setData(chatMapper.fromPostListToPostResponseList(posts));
        }
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
    @GetMapping(value = "/list-news", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<List<NewsResponse>> getNews() {
        BaseResponse<List<NewsResponse>> baseResponse = new BaseResponse<>();
        List<News> news = newsRepository.findAll();
        baseResponse.setData(newsMapper.fromNewsListToNewsResponseList(news));
        baseResponse.setResult(true);
        return baseResponse;
    }

    @PostMapping(value = "/add-favorite/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<String> addFavorite(@PathVariable long id) {
        BaseResponse<String> baseResponse = new BaseResponse<>();
        Field field = fieldRepository.findById(id).orElse(null);
        if (field == null){
            baseResponse.setResult(false);
            return baseResponse;
        }
        User user = userRepository.findById(getUserDetails().getId()).orElse(null);
        if (user == null){
            baseResponse.setResult(false);
            return baseResponse;
        }
        user.getFavorite().add(field);
        userRepository.save(user);
        baseResponse.setResult(true);
        return baseResponse;
    }

    @PostMapping(value = "/change-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<String> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        BaseResponse<String> baseResponse = new BaseResponse<>();
        User user = userRepository.findById(getUserDetails().getId()).orElse(null);
        if (user == null){
            baseResponse.setResult(false);
            return baseResponse;
        }
        if (!encoder.matches(request.getOldPassword(),user.getPassword())){
            baseResponse.setResult(false);
            baseResponse.setMessage("Mật khẩu cũ không chính xác");
            return baseResponse;
        }
        user.setPassword(encoder.encode(request.getNewPassword()));
        userRepository.save(user);
        baseResponse.setMessage("Thay đổi mật khẩu thành công");
        baseResponse.setResult(true);
        return baseResponse;
    }
    @GetMapping(value = "/get-favorite", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<Set<FieldResponse>> getFavorite() {
        BaseResponse<Set<FieldResponse>> baseResponse = new BaseResponse<>();
        User user = userRepository.findById(getUserDetails().getId()).orElse(null);
        if (user == null){
            baseResponse.setResult(false);
            return baseResponse;
        }
        Set<FieldResponse> result = universityMapper.fromFieldListToFieldListResponse(user.getFavorite());
        baseResponse.setData(result);
        baseResponse.setResult(true);
        return baseResponse;
    }



}
