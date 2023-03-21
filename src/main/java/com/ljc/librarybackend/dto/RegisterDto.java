package com.ljc.librarybackend.dto;

import com.ljc.librarybackend.pojo.entity.ReaderCard;
import lombok.Data;

@Data
public class RegisterDto extends ReaderCard {
    private String captcha;
}
