@double11
Feature: 雙十一活動定價規則 — 同品每滿 10 件 20% 折扣
  為了在雙十一活動提供清楚且可驗證的折扣行為
  作為購物顧客
  我想當同一種商品每滿 10 件時，該 10 件以 8 折計價

  Background:
    Given 雙十一優惠活動已啟動

  Rule: 同一種商品每買 10 件，該 10 件的價格總和享有 20% 折扣

  # 來自計劃文件：購買 12 件要價 100 元的襪子 → 10x100x80% + 2x100 = 1000
  Scenario: 同一商品 12 件（僅 10 件享 8 折）
    And 有以下商品加入購物車:
      | productName | quantity | unitPrice |
      | 襪子          | 12       | 100       |
    When 系統計算訂單金額
    Then 訂單金額應為:
      | totalAmount |
      | 1000        |

  # 來自計劃文件：購買 27 件要價 100 元的襪子 → 10x100x80% + 10x100x80% + 7x100 = 2300
  Scenario: 同一商品 27 件（兩組 10 件享 8 折，其餘原價）
    And 有以下商品加入購物車:
      | productName | quantity | unitPrice |
      | 襪子          | 27       | 100       |
    When 系統計算訂單金額
    Then 訂單金額應為:
      | totalAmount |
      | 2300        |

  # 來自計劃文件：不同商品各 1 件（共 10 件） → 無折扣，因非同一品項
  Scenario: 十種不同商品各 1 件（無折扣）
    And 有以下商品加入購物車:
      | productName | quantity | unitPrice |
      | A            | 1        | 100       |
      | B            | 1        | 100       |
      | C            | 1        | 100       |
      | D            | 1        | 100       |
      | E            | 1        | 100       |
      | F            | 1        | 100       |
      | G            | 1        | 100       |
      | H            | 1        | 100       |
      | I            | 1        | 100       |
      | J            | 1        | 100       |
    When 系統計算訂單金額
    Then 訂單金額應為:
      | totalAmount |
      | 1000        |

