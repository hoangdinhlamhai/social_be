package com.hdlh.social_be.mapper;

import com.hdlh.social_be.domain.dto.ai.recommendation.UserAiRequestProfileDTO;
import org.springframework.stereotype.Component;
import com.hdlh.social_be.domain.User;

import java.util.List;
import java.util.stream.Collectors;


@Component // Đánh dấu là một Spring Bean để có thể inject
public class UserMapper {
    public UserAiRequestProfileDTO toAiRequestProfileDTO(User user) {
        if (user == null) {
            return null;
        }
        String fullName = user.getFirstName() + " " + user.getLastName();

        //lay danh sach ten skill
        List<String> skillNames = user.getSkills().stream()
                .map(skill -> skill.getSkillName())
                .collect(Collectors.toList());

        return new UserAiRequestProfileDTO(
                user.getId(),
                fullName,
                skillNames
        );
    }

    public List<UserAiRequestProfileDTO> toAiProfileDtoList(List<User> users) {
        return users.stream()
                .map(this::toAiRequestProfileDTO)
                .collect(Collectors.toList());
    }
}
