package fzu.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tran {
    private Long id;
    private String account_info_id;
    private Double pre_tran_balance;
    private Double tran_amount;
    private Double post_tran_balance;
    private Date date;
    private Integer type;
}
