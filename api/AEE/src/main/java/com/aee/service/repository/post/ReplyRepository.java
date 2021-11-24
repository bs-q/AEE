package com.aee.service.repository.post;

import com.aee.service.models.post.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply,Long> {
    Page<Reply> findByPostIdOrderByCreatedDate(Long postId,Pageable paging);
}
