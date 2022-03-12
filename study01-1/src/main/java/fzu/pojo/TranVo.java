package fzu.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranVo {
    private Float tran_amount;
    private Date date;
    private Integer type;
    private Account account;
}
