# AGENT.md — BDD 核心原則指引

> 本文件用於團隊在 **討論、規劃、拆解需求或設計功能** 時使用，  
> 目標是確保所有決策與實作，都遵循 BDD 大師們所倡導的核心原則。

---

## 🌟 核心原則
Feature: 以行為為中心 (Behavior not code)
  為了確保程式碼直接支持業務價值
  作為團隊成員
  我想先定義系統應該如何行為，而不是如何實作

  Scenario: 定義期望行為而非技術細節
    Given 我們收到一個新需求
    When 我們討論這個需求時
    Then 我們描述的是使用者能完成的行為
    And 我們不先討論要用哪種演算法或資料結構

Feature: 跨角色共用語言 (Ubiquitous Language)
  為了避免誤解需求
  作為業務、開發與測試成員
  我想用一種大家都能理解的語言描述需求

  Scenario: 用 Gherkin 描述需求
    Given 產品經理提出一項新功能
    When 我們撰寫這項功能的需求描述
    Then 我們使用 Given/When/Then 的格式
    And 所有團隊角色都能理解內容

Feature: 例子驅動的需求澄清 (Example-Driven Discovery)
  為了確保我們理解一致
  作為團隊
  我想用具體例子來釐清抽象規則

  Scenario: 使用 Example Mapping 探討需求
    Given 我們面臨一個複雜的業務規則
    When 我們在會議中蒐集真實案例
    Then 我們歸納出相關規則與例外情況
    And 轉寫為清楚的場景

Feature: 活的文件 (Living Documentation)
  為了讓文件永遠與程式碼一致
  作為團隊
  我想把場景當作可執行的規格

  Scenario: 同步需求與程式碼
    Given 我們寫好一個 feature 檔
    When Cucumber 執行這個 feature 檔
    Then 它會驗證系統行為正確
    And 場景本身就是最新的需求文件

Feature: Outside-In 開發
  為了確保每個模組都支援使用者價值
  作為開發者
  我想從使用者最外層流程開始開發

  Scenario: 由外而內分解
    Given 我們要實作一個新功能
    When 我們先定義整體使用流程的預期行為
    Then 我們再逐層定義內部模組的行為與測試

Feature: 三方會談與協作 (Three Amigos)
  為了避免需求誤解
  作為團隊
  我想在撰寫場景前先與所有角色對齊理解

  Scenario: 三方共同討論需求
    Given 一個尚未實作的新需求
    When 產品、開發與測試一起開會討論
    Then 我們用共用語言釐清需求與期望行為
    And 所有人都認同場景內容才開始實作
    And 我們預期這樣的對話會在開發週期中持續進行
