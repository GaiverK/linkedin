package com.ra.course.linkedin.web.mapper;

import com.ra.course.linkedin.model.entity.other.Message;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.web.dto.message.MessageSendDto;
import com.ra.course.linkedin.web.utils.ControllerUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MessageMapper extends PropertyMap<MessageSendDto, Message> {

    private final ControllerUtils controllerUtils;

    private final Converter<String, List<Member>> strIdsToMembList = new Converter<>() {
        @Override
        public List<Member> convert(final MappingContext<String, List<Member>> context) {
            return context.getSource() == null ? null : Arrays.stream(context.getSource().split(","))
                    .map(Long::parseLong)
                    .map(controllerUtils::getMemberById)
                    .collect(Collectors.toList());
        }
    };

    private final Converter<Long, Member> authorIdToMember = new Converter<>() {
        @Override
        public Member convert(final MappingContext<Long, Member> context) {
            return context.getSource() == null ? null : controllerUtils.getMemberById(context.getSource());
        }
    };

    @Override
    protected void configure() {
        using(strIdsToMembList).map(source.getSentTo()).setSendTo(null);
        using(authorIdToMember).map(source.getAuthorId()).setAuthor(null);
    }
}
