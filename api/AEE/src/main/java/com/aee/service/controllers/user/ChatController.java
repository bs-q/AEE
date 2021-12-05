package com.aee.service.controllers.user;

import com.aee.service.controllers.base.BaseController;
import com.aee.service.mapper.ChatMapper;
import com.aee.service.models.User;
import com.aee.service.models.post.Post;
import com.aee.service.models.post.Reply;
import com.aee.service.payload.request.CreatePostRequest;
import com.aee.service.payload.request.ReplyRequest;
import com.aee.service.payload.response.BasePagingResponse;
import com.aee.service.payload.response.BaseResponse;
import com.aee.service.payload.response.PostResponse;
import com.aee.service.payload.response.ReplyResponse;
import com.aee.service.repository.post.PostRepository;
import com.aee.service.repository.post.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/u/discussion")
public class ChatController extends BaseController {

    @Autowired
    ChatMapper chatMapper;

    @Autowired
    PostRepository postRepository;

    @Autowired
    ReplyRepository replyRepository;


    @PostMapping(value = "/create-post", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<String> createPost(@Valid @RequestBody CreatePostRequest request) {
        BaseResponse<String> baseResponse = new BaseResponse<>();
        Post post = new Post();
        post.setContent(request.getContent());
        post.setTitle(request.getTitle());
        User creator = new User();
        creator.setId(getUserDetails().getId());
        post.setCreator(creator);
        postRepository.save(post);
        baseResponse.setResult(true);
        return baseResponse;
    }

    @Transactional
    @PostMapping(value = "/reply-post", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<String> replyPost(@Valid @RequestBody ReplyRequest request) {
        BaseResponse<String> baseResponse = new BaseResponse<>();
        Post post = postRepository.findById(request.getPostId()).orElse(null);
        if (post == null){
            baseResponse.setData("Post not found");
            return baseResponse;
        }
        Reply reply = new Reply();
        reply.setContent(request.getContent());
        reply.setPost(post);
        User user = new User();
        user.setId(getUserDetails().getId());
        reply.setUser(user);
        replyRepository.save(reply);
        baseResponse.setResult(true);
        baseResponse.setMessage("Reply success");
        return baseResponse;
    }
    @GetMapping(value = "/find-post", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<PostResponse> findPost(@Valid @RequestParam Long postId) {
        BaseResponse<PostResponse> baseResponse = new BaseResponse<>();
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null){
            baseResponse.setMessage("Post not found");
            return baseResponse;
        }
        baseResponse.setResult(true);
        baseResponse.setData(chatMapper.fromPostToPostResponse(post));
        baseResponse.setMessage("Find post success");
        return baseResponse;
    }

    @GetMapping(value = "/get-replies", produces = MediaType.APPLICATION_JSON_VALUE)
    public BasePagingResponse<List<ReplyResponse>> listReplies(@Valid @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "3") int size,
                                                               @RequestParam Long postId) {
        BasePagingResponse<List<ReplyResponse>> baseResponse = new BasePagingResponse<>();
        Pageable paging = PageRequest.of(page, size);
        Page<Reply> replies = replyRepository.findByPostIdOrderByCreatedDate(postId,paging);
        if (replies.hasContent()){
            baseResponse.setData(chatMapper.fromReplyListToReplyResponseList(replies.getContent()));
        } else {
            baseResponse.setData(new ArrayList<>());
        }
        baseResponse.setCurrentPage(replies.getNumber());
        baseResponse.setTotalPages(replies.getTotalPages());
        baseResponse.setTotalItems((int) replies.getTotalElements());
        baseResponse.setResult(true);
        return baseResponse;
    }

    @GetMapping(value = "/get-posts", produces = MediaType.APPLICATION_JSON_VALUE)
    public BasePagingResponse<List<PostResponse>> listPosts(@Valid @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "3") int size) {
        BasePagingResponse<List<PostResponse>>baseResponse = new BasePagingResponse<>();
        Pageable paging = PageRequest.of(page, size);
        Page<Post> post = postRepository.findAllByOrderByCreatedDateDesc(paging);
        baseResponse.setData(chatMapper.fromPostListToPostResponseList(post.getContent()));
        baseResponse.setCurrentPage(post.getNumber());
        baseResponse.setTotalPages(post.getTotalPages());
        baseResponse.setTotalItems((int) post.getTotalElements());
        baseResponse.setResult(true);
        return baseResponse;
    }
}

