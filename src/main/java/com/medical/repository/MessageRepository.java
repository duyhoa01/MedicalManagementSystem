package com.medical.repository;

import com.medical.model.Message;
import com.medical.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface MessageRepository extends JpaRepository<Message,Long> {

    @Query("from Message m where ( m.sender.id = :sender and m.receiver.id = :receiver) or (m.sender.id = :receiver and m.receiver.id = :sender) order by m.date desc")
    Page<Message> findAllOfUserandUser(@Param("sender") Long sender, @Param("receiver") Long receiver, Pageable pageable);

    @Query("from Message m where m.sender.id = :id or m.receiver.id = :id order by m.date desc")
    List<Message> findListUserChat(@Param("id") Long id);

}
