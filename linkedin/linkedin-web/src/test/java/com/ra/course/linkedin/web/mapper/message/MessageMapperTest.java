package com.ra.course.linkedin.web.mapper.message;

import com.ra.course.linkedin.model.entity.other.Message;
import com.ra.course.linkedin.model.entity.person.Member;
import com.ra.course.linkedin.web.dto.message.MessageSendDto;
import com.ra.course.linkedin.web.mapper.MessageMapper;
import com.ra.course.linkedin.web.utils.ControllerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
class MessageMapperTest {

    public static final int EXPECTED_LIST_SIZE = 1;
    public static final long DEFAULT_TEST_ID = 1L;
    public static final String MEMBER_NAME = "Pushkin";
    public static final String MEMBER_PHONE = "+380506666666";
    @Autowired
    private MessageMapper messageMapper;
    @MockBean
    private ControllerUtils controllerUtils;

    @Autowired
    private ModelMapper modelMapper;

    private Member member;
    private MessageSendDto dto;

    @BeforeEach
    void setUp() {
        member = createMember();
        dto = getMessageSendDto();
        if (modelMapper.getTypeMap(MessageSendDto.class, Message.class) == null) {
            modelMapper.addMappings(messageMapper);
        }
    }

    @Test
    @DisplayName("When stringIds sentTo exists then sentTo list size should be 1")
    void testMessageMapperFromDTOToMessageAndSentToExists() {
        //given
        dto.setSentTo(String.valueOf(member.getId()));
        //when
        when(controllerUtils.getMemberById(anyLong())).thenReturn(member);
        Message message = modelMapper.map(dto, Message.class);

        //then
        assertEquals(EXPECTED_LIST_SIZE, message.getSendTo().size());
    }

    @Test
    @DisplayName("When stringIds sentTo not exists then sentTo list size should be null")
    void testMessageMapperFromDTOToMessageAndSentToNotExists() {
        //when
        when(controllerUtils.getMemberById(anyLong())).thenReturn(member);
        Message message = modelMapper.map(dto, Message.class);

        //then
        assertNull(message.getSendTo());
    }

    @Test
    @DisplayName("When authorId not exists then message member should be null")
    void testMessageMapperFromDTOToMessageAndAuthorIdNull() {
        //given
        dto.setAuthorId(null);
        //when
        Message message = modelMapper.map(dto, Message.class);
        //then
        assertNull(message.getAuthor());
    }

    private MessageSendDto getMessageSendDto() {
        MessageSendDto dto = new MessageSendDto();
        dto.setSentTo(null);
        dto.setAuthorId(member.getId());

        return dto;
    }

    private Member createMember() {
        return Member.builder()
                .id(DEFAULT_TEST_ID)
                .name(MEMBER_NAME)
                .phone(MEMBER_PHONE)
                .build();
    }
}