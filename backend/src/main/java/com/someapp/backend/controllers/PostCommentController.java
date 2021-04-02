package com.someapp.backend.controllers;

import com.someapp.backend.entities.PostComment;
import com.someapp.backend.repositories.PostCommentRepository;
import com.someapp.backend.repositories.PostRepository;
import com.someapp.backend.repositories.UserRepository;
import com.someapp.backend.util.customExceptions.ResourceNotFoundException;
import com.someapp.backend.util.requests.SendPostCommentRequest;
import com.someapp.backend.util.requests.UUIDRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
public class PostCommentController {

    @Autowired
    PostCommentRepository postCommentRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/posts/comments/{postId}")
    public List<PostComment> getPostCommentsByPostId(@PathVariable UUID postId) {
        if (postRepository.findById(postId).isPresent()) {
            return postRepository.getById(postId).getPostComments();
        } else {
            throw new ResourceNotFoundException("Post was not found with given uuid");
        }
    }

    @PostMapping("/posts/comments")
    public PostComment sendNewPostComment(@Valid @RequestBody SendPostCommentRequest sendPostCommentRequest,
                                   BindingResult bindingResult) throws BindException {
        // IF VALIDATION ERRORS
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);

        // IF POSTID AND USERID ARE CORRECT, SAVE NEW POST COMMENT
        } else if (postRepository.findById(sendPostCommentRequest.getPostId()).isPresent()
                && userRepository.findById(sendPostCommentRequest.getUserId()).isPresent()) {

            return postCommentRepository.save(new PostComment(sendPostCommentRequest.getPostComment(),
                        postRepository.getById(sendPostCommentRequest.getPostId()),
                        userRepository.getById(sendPostCommentRequest.getUserId())));

        // IF POST OR USER WEREN'T FOUND WITH GIVEN UUID
        } else {
            throw new ResourceNotFoundException("Post or user was not found with given uuid");
        }
    }

    @DeleteMapping("/posts/comments/{postCommentId}")
    public UUIDRequest deletePostCommentById(@PathVariable UUID postCommentId) throws ResourceNotFoundException {
        try {
            postCommentRepository.deleteById(postCommentId);
            return new UUIDRequest(postCommentId);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Post comment was not found with given uuid.");
        }
    }
}
