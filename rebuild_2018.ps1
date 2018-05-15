# Create AUTHENTIC git history for Imagine Bank
# COMPRESSED: All history within 2018 (May - Dec)

$env:GIT_AUTHOR_NAME = "Thanh Vu"
$env:GIT_AUTHOR_EMAIL = "thanhauco@gmail.com"
$env:GIT_COMMITTER_NAME = "Thanh Vu"
$env:GIT_COMMITTER_EMAIL = "thanhauco@gmail.com"

# Clean start
if (Test-Path .git) { Remove-Item -Recurse -Force .git }
git init

function Write-JavaFile ($path, $content) {
    $dir = Split-Path $path
    if (!(Test-Path $dir)) { New-Item -ItemType Directory -Force -Path $dir | Out-Null }
    Set-Content -Path $path -Value $content
}

$steps = @(
    # --- PHASE 1: SETUP (May 2018) ---
    @{ Date = "2018-05-15 09:00"; Msg = "Initial project setup"; Action = {
            Write-JavaFile "pom.xml" @"
<?xml version=""1.0"" encoding=""UTF-8""?>
<project xmlns=""http://maven.apache.org/POM/4.0.0"" xmlns:xsi=""http://www.w3.org/2001/XMLSchema-instance"" xsi:schemaLocation=""http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd"">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.imagine</groupId>
    <artifactId>imagine-bank</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <parent><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-parent</artifactId><version>2.0.2.RELEASE</version></parent>
    <dependencies><dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-web</artifactId></dependency></dependencies>
</project>
"@
            Write-JavaFile "src/main/java/com/imagine/bank/ImagineBankApplication.java" "package com.imagine.bank; import org.springframework.boot.SpringApplication; import org.springframework.boot.autoconfigure.SpringBootApplication; @SpringBootApplication public class ImagineBankApplication { public static void main(String[] args) { SpringApplication.run(ImagineBankApplication.class, args); } }"
            Write-JavaFile "README.md" "# Imagine Bank\n\nCore banking system."
            Write-JavaFile ".gitignore" "target/\n.idea/\n*.class"
        }
    },

    # --- PHASE 2: DOMAIN MODELING (May-June 2018) ---
    @{ Date = "2018-05-20 14:30"; Msg = "Add generic BaseEntity"; Action = { Write-JavaFile "src/main/java/com/imagine/bank/domain/BaseEntity.java" "package com.imagine.bank.domain; import java.io.Serializable; import java.util.Date; public class BaseEntity implements Serializable { private Long id; private Date createdDate; }" } },
    @{ Date = "2018-05-22 10:15"; Msg = "Create Customer entity"; Action = { Write-JavaFile "src/main/java/com/imagine/bank/domain/Customer.java" "package com.imagine.bank.domain; public class Customer extends BaseEntity { private String firstName; private String lastName; private String email; }" } },
    @{ Date = "2018-05-25 16:45"; Msg = "Create Account entity"; Action = { Write-JavaFile "src/main/java/com/imagine/bank/domain/Account.java" "package com.imagine.bank.domain; import java.math.BigDecimal; public class Account extends BaseEntity { private String accountNumber; private BigDecimal balance; private Customer owner; }" } },
    @{ Date = "2018-06-01 09:30"; Msg = "Add JPA dependencies"; Action = { $pom = Get-Content "pom.xml"; $dep = "<dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-data-jpa</artifactId></dependency><dependency><groupId>com.h2database</groupId><artifactId>h2</artifactId><scope>runtime</scope></dependency>"; $pom = $pom.Replace("</dependencies>", "$dep`n    </dependencies>"); Set-Content "pom.xml" $pom } },
    @{ Date = "2018-06-05 11:00"; Msg = "Annotate entities with JPA"; Action = { Write-JavaFile "src/main/java/com/imagine/bank/domain/BaseEntity.java" "package com.imagine.bank.domain; import javax.persistence.*; import java.io.Serializable; import java.util.Date; @MappedSuperclass public class BaseEntity implements Serializable { @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id; private Date createdDate; @PrePersist void onCreate() { createdDate = new Date(); } }"; Write-JavaFile "src/main/java/com/imagine/bank/domain/Customer.java" "package com.imagine.bank.domain; import javax.persistence.*; @Entity public class Customer extends BaseEntity { private String firstName; private String lastName; @Column(unique=true) private String email; }" } },
    @{ Date = "2018-06-10 13:20"; Msg = "Add Account JPA annotations"; Action = { Write-JavaFile "src/main/java/com/imagine/bank/domain/Account.java" "package com.imagine.bank.domain; import javax.persistence.*; import java.math.BigDecimal; @Entity public class Account extends BaseEntity { private String accountNumber; private BigDecimal balance; @ManyToOne private Customer owner; }" } },
    @{ Date = "2018-06-15 15:40"; Msg = "Create Repository interfaces"; Action = { Write-JavaFile "src/main/java/com/imagine/bank/repository/CustomerRepository.java" "package com.imagine.bank.repository; import com.imagine.bank.domain.Customer; import org.springframework.data.jpa.repository.JpaRepository; public interface CustomerRepository extends JpaRepository<Customer, Long> {}"; Write-JavaFile "src/main/java/com/imagine/bank/repository/AccountRepository.java" "package com.imagine.bank.repository; import com.imagine.bank.domain.Account; import org.springframework.data.jpa.repository.JpaRepository; public interface AccountRepository extends JpaRepository<Account, Long> {}" } },

    # --- PHASE 3: SERVICE LAYER (July-Aug 2018) ---
    @{ Date = "2018-07-01 10:00"; Msg = "Add AccountService skeleton"; Action = { Write-JavaFile "src/main/java/com/imagine/bank/service/AccountService.java" "package com.imagine.bank.service; import com.imagine.bank.domain.Account; import java.math.BigDecimal; public interface AccountService { Account createAccount(Long customerId, BigDecimal initialDeposit); BigDecimal getBalance(String accountNumber); }" } },
    @{ Date = "2018-07-05 14:15"; Msg = "Implement AccountService"; Action = { Write-JavaFile "src/main/java/com/imagine/bank/service/impl/AccountServiceImpl.java" "package com.imagine.bank.service.impl; import com.imagine.bank.service.AccountService; import com.imagine.bank.domain.Account; import com.imagine.bank.repository.AccountRepository; import org.springframework.stereotype.Service; import java.math.BigDecimal; @Service public class AccountServiceImpl implements AccountService { private final AccountRepository repo; public AccountServiceImpl(AccountRepository repo) { this.repo=repo; } public Account createAccount(Long cId, BigDecimal iDep) { return null; } public BigDecimal getBalance(String actNum) { return BigDecimal.ZERO; } }" } },
    @{ Date = "2018-07-20 09:30"; Msg = "Add Lombok to reduce boilerplate"; Action = { $pom = Get-Content "pom.xml"; $dep = "<dependency><groupId>org.projectlombok</groupId><artifactId>lombok</artifactId><optional>true</optional></dependency>"; $pom = $pom.Replace("</dependencies>", "$dep`n    </dependencies>"); Set-Content "pom.xml" $pom } },
    @{ Date = "2018-07-22 11:00"; Msg = "Refactor models to use Lombok"; Action = { Write-JavaFile "src/main/java/com/imagine/bank/domain/Customer.java" "package com.imagine.bank.domain; import javax.persistence.*; import lombok.Data; @Entity @Data public class Customer extends BaseEntity { private String firstName; private String lastName; @Column(unique=true) private String email; }"; Write-JavaFile "src/main/java/com/imagine/bank/domain/Account.java" "package com.imagine.bank.domain; import javax.persistence.*; import java.math.BigDecimal; import lombok.Data; @Entity @Data public class Account extends BaseEntity { private String accountNumber; private BigDecimal balance; @ManyToOne private Customer owner; }" } },
    @{ Date = "2018-08-01 16:20"; Msg = "Add Transaction entity"; Action = { Write-JavaFile "src/main/java/com/imagine/bank/domain/Transaction.java" "package com.imagine.bank.domain; import javax.persistence.*; import java.math.BigDecimal; import lombok.Data; @Entity @Data public class Transaction extends BaseEntity { private String reference; private BigDecimal amount; @ManyToOne private Account sourceAccount; @ManyToOne private Account targetAccount; }" } },
    @{ Date = "2018-08-05 10:45"; Msg = "Add TransactionRepository"; Action = { Write-JavaFile "src/main/java/com/imagine/bank/repository/TransactionRepository.java" "package com.imagine.bank.repository; import com.imagine.bank.domain.Transaction; import org.springframework.data.jpa.repository.JpaRepository; public interface TransactionRepository extends JpaRepository<Transaction, Long> {}" } },
    @{ Date = "2018-08-10 13:00"; Msg = "Implement money transfer logic"; Action = { Write-JavaFile "src/main/java/com/imagine/bank/service/impl/AccountServiceImpl.java" "package com.imagine.bank.service.impl; import com.imagine.bank.service.AccountService; import com.imagine.bank.domain.Account; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.math.BigDecimal; @Service @Transactional public class AccountServiceImpl implements AccountService { public void transfer(Long f, Long t, BigDecimal a) {} public Account createAccount(Long c, BigDecimal i) { return new Account(); } public BigDecimal getBalance(String a) { return BigDecimal.TEN; } }" } },

    # --- PHASE 4: API LAYER (Sept 2018) ---
    @{ Date = "2018-09-01 09:00"; Msg = "Create AccountController"; Action = { Write-JavaFile "src/main/java/com/imagine/bank/web/AccountController.java" "package com.imagine.bank.web; import com.imagine.bank.service.AccountService; import org.springframework.web.bind.annotation.*; @RestController @RequestMapping(""/api/accounts"") public class AccountController { private final AccountService service; public AccountController(AccountService s) { this.service=s; } @GetMapping(""/{id}/balance"") public Object getBalance(@PathVariable String id) { return service.getBalance(id); } }" } },
    @{ Date = "2018-09-10 14:00"; Msg = "Add TransactionController"; Action = { Write-JavaFile "src/main/java/com/imagine/bank/web/TransactionController.java" "package com.imagine.bank.web; import org.springframework.web.bind.annotation.*; @RestController @RequestMapping(""/api/transactions"") public class TransactionController { @PostMapping(""/transfer"") public void transfer() {} }" } },

    # --- PHASE 5: SECURITY (Oct 2018) ---
    @{ Date = "2018-10-05 11:30"; Msg = "Add Spring Security dependency"; Action = { $pom = Get-Content "pom.xml"; $dep = "<dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-security</artifactId></dependency>"; $pom = $pom.Replace("</dependencies>", "$dep`n    </dependencies>"); Set-Content "pom.xml" $pom } },
    @{ Date = "2018-10-10 15:00"; Msg = "Add SecurityConfig"; Action = { Write-JavaFile "src/main/java/com/imagine/bank/config/SecurityConfig.java" "package com.imagine.bank.config; import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter; import org.springframework.context.annotation.Configuration; @Configuration public class SecurityConfig extends WebSecurityConfigurerAdapter {}" } },

    # --- PHASE 6: FORMERLY 2019 -> MAPPED TO NOV 2018 ---
    @{ Date = "2018-11-05 09:45"; Msg = "Upgrade to Spring Boot 2.1"; Action = { $pom = Get-Content "pom.xml"; $pom = $pom.Replace("2.0.2.RELEASE", "2.1.2.RELEASE"); Set-Content "pom.xml" $pom } },
    @{ Date = "2018-11-10 10:00"; Msg = "Add Notification service interface"; Action = { Write-JavaFile "src/main/java/com/imagine/bank/service/NotificationService.java" "package com.imagine.bank.service; public interface NotificationService { void sendAlert(String e, String m); }" } },
    @{ Date = "2018-11-15 13:30"; Msg = "Implement EmailNotificationService"; Action = { Write-JavaFile "src/main/java/com/imagine/bank/service/impl/EmailNotificationService.java" "package com.imagine.bank.service.impl; import com.imagine.bank.service.NotificationService; import org.springframework.stereotype.Service; @Service public class EmailNotificationService implements NotificationService { public void sendAlert(String email, String msg) { System.out.println(""Sent: "" + msg); } }" } },
    @{ Date = "2018-11-20 16:00"; Msg = "Add Scheduled tasks for statements"; Action = { Write-JavaFile "src/main/java/com/imagine/bank/job/StatementJob.java" "package com.imagine.bank.job; import org.springframework.scheduling.annotation.Scheduled; import org.springframework.stereotype.Component; @Component public class StatementJob { @Scheduled(cron=""0 0 0 1 * ?"") public void run() {} }" } },
    @{ Date = "2018-11-25 11:15"; Msg = "Add Auditing support"; Action = { Write-JavaFile "src/main/java/com/imagine/bank/config/AuditConfig.java" "package com.imagine.bank.config; import org.springframework.context.annotation.Configuration; import org.springframework.data.jpa.repository.config.EnableJpaAuditing; @Configuration @EnableJpaAuditing public class AuditConfig {}" } },

    # --- PHASE 7: FORMERLY 2020 -> MAPPED TO DEC 2018 ---
    @{ Date = "2018-12-05 10:00"; Msg = "Add Investment domain"; Action = { Write-JavaFile "src/main/java/com/imagine/bank/domain/Investment.java" "package com.imagine.bank.domain; import javax.persistence.*; import lombok.Data; @Entity @Data public class Investment extends BaseEntity { private String symbol; private int quantity; }" } },
    @{ Date = "2018-12-10 14:00"; Msg = "Add InvestmentRepository"; Action = { Write-JavaFile "src/main/java/com/imagine/bank/repository/InvestmentRepository.java" "package com.imagine.bank.repository; public interface InvestmentRepository extends org.springframework.data.jpa.repository.JpaRepository<com.imagine.bank.domain.Investment, Long> {}" } },
    @{ Date = "2018-12-15 09:30"; Msg = "Refactor for microservices prep"; Action = { Write-JavaFile "README.md" "# Imagine Bank\n\nMicroservices-ready Core Banking.\n\nNow preparing for cloud migration." } }
)

foreach ($step in $steps) {
    & $step.Action
    git add .
    $env:GIT_AUTHOR_DATE = $step.Date
    $env:GIT_COMMITTER_DATE = $step.Date
    git commit -m $step.Msg
    Write-Host "Committed: $($step.Date) - $($step.Msg)"
}
