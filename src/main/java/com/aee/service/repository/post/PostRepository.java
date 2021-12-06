package com.aee.service.repository.post;

import com.aee.service.models.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {
    Page<Post> findAllByOrderByCreatedDateDesc(Pageable pageable);

    List<Post> findAllByTitleContaining(String title);

    List<Post> findAllByCreatorId(Long id);
}
