package com.trevorgowing.expenselist.expense;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
class ExpenseDTO implements Serializable {

  private Long id;
  private Instant date;
  private BigDecimal amount;
  private BigDecimal vat;
  private String reason;
  private Long userId;

  static ExpenseDTO create(
      Long id, Instant date, BigDecimal amount, BigDecimal vat, String reason, Long userId) {
    return new ExpenseDTO(id, date, amount, vat, reason, userId);
  }

  @Override
  public boolean equals(Object objectToCompareTo) {
    if (this == objectToCompareTo) return true;
    if (objectToCompareTo == null || getClass() != objectToCompareTo.getClass()) return false;
    ExpenseDTO expenseToCompareTo = (ExpenseDTO) objectToCompareTo;
    return Objects.equals(getId(), expenseToCompareTo.getId())
        && Objects.equals(getDate(), expenseToCompareTo.getDate())
        && Objects.equals(getAmount(), expenseToCompareTo.getAmount())
        && Objects.equals(getVat(), expenseToCompareTo.getVat())
        && Objects.equals(getUserId(), expenseToCompareTo.getUserId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getDate(), getAmount(), getVat(), getUserId());
  }
}
