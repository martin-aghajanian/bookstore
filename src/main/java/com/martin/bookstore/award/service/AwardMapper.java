package com.martin.bookstore.award.service;

import com.martin.bookstore.award.dto.AwardRequestDto;
import com.martin.bookstore.award.dto.AwardResponseDto;
import com.martin.bookstore.award.persistence.entity.Award;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface AwardMapper {

    Award asEntity(AwardRequestDto awardRequest);

    AwardResponseDto asOutput(Award award);

    List<AwardResponseDto> asOutput(List<Award> awards);

    void update(@MappingTarget Award entity, AwardRequestDto awardRequest);
}
