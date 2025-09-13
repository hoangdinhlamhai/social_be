package com.hdlh.social_be.repository;

import com.hdlh.social_be.domain.ChatRoom;
import com.hdlh.social_be.domain.ChatRoomParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    // method find mình tự viết ra sẽ mặc định trả về optional
    //tự động suy luận kiểu trả về là Optional<T> cho các phương thức tuân thủ quy ước findBy... và được dự kiến sẽ trả về một hoặc không có kết quả nào.
    @Query("SELECT cr FROM ChatRoom cr " +
            "WHERE (SELECT COUNT(p) FROM cr.participants p) = 2 " +
            "AND EXISTS (SELECT p FROM cr.participants p WHERE p.user.id = :user1Id) " +
            "AND EXISTS (SELECT p FROM cr.participants p WHERE p.user.id = :user2Id)")
    Optional<ChatRoom> findChatRoomByTwoUserIds(@Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id);

    @Query("SELECT DISTINCT cr FROM ChatRoom cr " +
            "JOIN FETCH cr.participants p " +
            "JOIN FETCH p.user u " +
            "WHERE cr.id IN (SELECT p2.chatRoom.id FROM ChatRoomParticipant p2 WHERE p2.user.id = :userId)")
    List<ChatRoom> findChatRoomsByUserIdWithDetails(@Param("userId") Long userId);
}
