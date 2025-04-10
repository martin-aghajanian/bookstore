package com.martin.bookstore.models.award.dto;

import com.martin.bookstore.models.award.persistence.entity.Award;
import org.springframework.stereotype.Component;

@Component
public class AwardMapper {
    public AwardDto toDto(Award award) {
        AwardDto awardDto = new AwardDto();
        awardDto.setId(award.getId());
        awardDto.setName(award.getName());
        return awardDto;
    }

    public Award toEntity(AwardDto awardDto) {
        Award award = new Award();
        award.setId(awardDto.getId());
        award.setName(awardDto.getName());
        return award;
    }
}
