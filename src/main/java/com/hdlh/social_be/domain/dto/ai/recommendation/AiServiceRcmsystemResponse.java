package com.hdlh.social_be.domain.dto.ai.recommendation;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record AiServiceRcmsystemResponse(
//        Jackson hãy map key recommended_user_ids trong JSON vào trường recommendedUserIds của Java (vì quy ước đặt tên khác nhau).
        // vì bên python trả về string (muốn thì chỉnh về Long cũng được)
        @JsonProperty("recommended_user_ids")
        List<String> recommendedUserIds
) {}
