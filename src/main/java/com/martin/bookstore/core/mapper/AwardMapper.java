package com.martin.bookstore.core.mapper;

import com.martin.bookstore.dto.AwardDto;
import com.martin.bookstore.entity.Award;
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
