package com.trevorgowing.expenselist.expense;

import com.trevorgowing.expenselist.common.persistence.AbstractAuditable;
import com.trevorgowing.expenselist.user.User;
import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "expenses")
class Expense extends AbstractAuditable<User, Long> {

  @Basic(optional = false)
  @Column(nullable = false)
  private Instant date;

  @Basic(optional = false)
  @Column(nullable = false, precision = 13, scale = 2)
  private BigDecimal amount;

  @Basic(optional = false)
  @Column(nullable = false, precision = 13, scale = 2)
  private BigDecimal vat;

  @Column(columnDefinition = "TEXT")
  private String reason;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false,
      foreignKey = @ForeignKey(name = "expenses_user_id_foreign_key"))
  private User user;

  private Expense(Long id) {
    super(id);
  }

  private Expense(Instant date, BigDecimal amount, String reason, User user) {
    calculateVatAndSetOthers(date, amount, reason, user);
  }

  private Expense(Long id, Instant date, BigDecimal amount, String reason, User user) {
    super(id);
    calculateVatAndSetOthers(date, amount, reason, user);
  }

  private void calculateVatAndSetOthers(Instant date, BigDecimal amount, String reason, User user) {
    this.date = date;
    this.vat = calculateVat(amount);
    this.amount = amount.subtract(this.vat);
    this.reason = reason;
    this.user = user;
  }

  private Expense(Long id, Instant date, BigDecimal amount, BigDecimal vat, String reason,
      User user) {
    super(id);
    this.date = date;
    this.amount = amount;
    this.vat = vat;
    this.reason = reason;
    this.user = user;
  }

  static Expense identifiedExpense(Long id) {
    return new Expense(id);
  }

  static Expense identifiedExpense(Long id, Instant date, BigDecimal amount, BigDecimal vat,
      String reason, User user) {
    return new Expense(id, date, amount, vat, reason, user);
  }

  static Expense calculateVatAndCreate(
      Long id, Instant date, BigDecimal amount, String reason, User user) {
    return new Expense(id, date, amount, reason, user);
  }

  static Expense calculateVatAndCreate(Instant date, BigDecimal amount, String reason, User user) {
    return new Expense(date, amount, reason, user);
  }

  static BigDecimal calculateVat(BigDecimal amount) {
    return new BigDecimal("0.2").multiply(amount);
  }
}

