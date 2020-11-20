package com.soft1851.pojo.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author crq
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewAdminBO {
    private String username;
    private String adminName;
    private String password;
    private String confirmPassword;
    private String img64;
    private String faceId;

}
