package com.martin.bookstore.mapper;

import com.martin.bookstore.entity.Award;
import com.martin.bookstore.dto.request.AwardRequestDto;
import com.martin.bookstore.dto.response.AwardResponseDto;
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
