package com.soft1851.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author crq
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FansCountsVO {
    Integer manCounts;
    Integer womanCounts;
}
