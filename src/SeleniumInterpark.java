import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SeleniumInterpark {
    private static WebDriver driver;
    private static String id = "";
    private static String pw = "";
    private static String bir = "";
    private static Boolean seatTF = false;
    private static Boolean rightSeat = false;
    private static Boolean isTwo = false;
    private static int nthFromBack = 1;
    private static Boolean byDistance = false;

    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        jFrame.setLocation(1000, 300);
        jFrame.setSize(250, 400);
        jFrame.setLayout(new FlowLayout());
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel idL = new JLabel("ID");
        JTextField idField = new JTextField(25);
        idField.setHorizontalAlignment(JTextField.CENTER);

        JLabel pwL = new JLabel("PW");
        JPasswordField pwField = new JPasswordField(25);
        pwField.setHorizontalAlignment(JTextField.CENTER);

        JLabel nthL = new JLabel("경기는 뒤에서 몇번째? (끝 = 1)");
        JTextField nthField = new JTextField(25);
        nthField.setHorizontalAlignment(JTextField.CENTER);
        nthField.setText("1");

        JLabel birL = new JLabel("생년월일 6자리");
        JTextField birField = new JTextField(25);
        birField.setHorizontalAlignment(JTextField.CENTER);

        JButton runButton = new JButton("로그인");
        runButton.setFont(new Font("맑은 고딕", Font.BOLD, 30));
        runButton.setPreferredSize(new java.awt.Dimension(200, 50));

        JButton pwVisibleButton = new JButton("비밀번호 보이기");
        pwVisibleButton.setFont(new Font("맑은 고딕", Font.BOLD, 10));
        pwVisibleButton.setPreferredSize(new java.awt.Dimension(200, 20));

        JLabel seatL = new JLabel("(2명 선택시에도 1명만 될 수 있음)");
        JPanel panel = new JPanel();
        JRadioButton[] radioButton = new JRadioButton[2];
        ButtonGroup buttonGroup = new ButtonGroup();
        radioButton[0] = new JRadioButton("좌");
        buttonGroup.add(radioButton[0]);
        panel.add(radioButton[0]);
        radioButton[1] = new JRadioButton("우");
        buttonGroup.add(radioButton[1]);
        panel.add(radioButton[1]);

        JPanel panel2 = new JPanel();
        JRadioButton[] radioButton2 = new JRadioButton[2];
        ButtonGroup buttonGroup2 = new ButtonGroup();
        radioButton2[0] = new JRadioButton("1명");
        buttonGroup2.add(radioButton2[0]);
        panel.add(radioButton2[0]);
        radioButton2[1] = new JRadioButton("2명");
        buttonGroup2.add(radioButton2[1]);
        panel.add(radioButton2[1]);

        // checkbox
        JCheckBox[] checkBox = new JCheckBox[1];
        checkBox[0] = new JCheckBox("중앙~거리순(설정시 느림)");

        radioButton[0].setSelected(true);
        radioButton2[0].setSelected(true);

        jFrame.add(idL);
        jFrame.add(idField);

        jFrame.add(pwL);
        jFrame.add(pwField);
        pwField.setEchoChar('*');
        jFrame.add(pwVisibleButton);

        jFrame.add(nthL);
        jFrame.add(nthField);

        jFrame.add(birL);
        jFrame.add(birField);

        jFrame.add(seatL);
        jFrame.add(panel);
        jFrame.add(panel2);
        jFrame.add(runButton);

        jFrame.add(checkBox[0]);

        jFrame.setVisible(true);

        pwVisibleButton.addActionListener(e -> pwField.setEchoChar(pwField.getEchoChar() == '*' ? (char) 0 : '*'));
        runButton.addActionListener(e -> {
            try {
                jFrame.setVisible(false);

                id = idField.getText();
                pw = pwField.getText();
                bir = birField.getText();
                nthFromBack = Integer.parseInt(nthField.getText());

                rightSeat = !radioButton[0].isSelected();
                isTwo = radioButton2[1].isSelected();

                byDistance = checkBox[0].isSelected();

                System.out.println(rightSeat);
                loginModule();
                ticketModule();
            } catch (NoSuchWindowException ex) {
                System.exit(0);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
    }

    private static void loginModule() {
        setDriver();
        Acting.loginProcess();
    }

    private static void ticketModule() throws InterruptedException {
        JOptionPane.showMessageDialog(null, "서버시간을 확인하고 이 창을 넘기세요");
        driver.navigate().refresh();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("/html/body/div[6]/div[4]/div[4]"))));

        Acting.btnClickProcess();

        Thread.sleep(300);
        List<String> tab = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tab.get(1));
        System.out.println(driver.getTitle());

        Acting.captchaType();

        while (true) {
            if (seatTF) {
                break;
            } else {
                driver.navigate().refresh();
                seatTF = Acting.seatSelect();
            }
        }
        Acting.phase0();
        Acting.phase1();
        Acting.phase2();
        Acting.phase3();
        Acting.phase4();
    }

    private static void setDriver() {
        //크롬드라이버 경로
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().setSize(new Dimension(800, 800));
        driver.get("https://accounts.interpark.com/authorize/ticket-pc?origin=https%3A%2F%2Fticket%2Einterpark%2Ecom%2FGate%2FTPLoginConfirmGate%2Easp%3FGroupCode%3D%26Tiki%3D%26Point%3D%26PlayDate%3D%26PlaySeq%3D%26HeartYN%3D%26TikiAutoPop%3D%26BookingBizCode%3D%26MemBizCD%3DWEBBR%26CPage%3DB%26GPage%3Dhttp%253A%252F%252Fticket%252Einterpark%252Ecom%252FContents%252FSports%252FGoodsInfo%253FSportsCode%253D07032%2526TeamCode%253DPE015");
    }

    private static class Acting {
        private static void loginProcess() {
            WebElement idBox = driver.findElement(By.id("userId"));
            WebElement pwBox = driver.findElement(By.id("userPwd"));
            WebElement login = driver.findElement(By.id("btn_login"));
            //아이디, 비밀번호
            idBox.sendKeys(id);
            pwBox.sendKeys(pw);
            login.click();
        }

        private static void btnClickProcess() {
            try {
                List<WebElement> btnColor_y = driver.findElements(By.className("BtnColor_Y"));
                //맨 끝에 get 활성화된 버튼 배열 인덱스(0~ )
                WebElement tarBtn = btnColor_y.get(btnColor_y.size() - nthFromBack);
                tarBtn.sendKeys(Keys.ENTER);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("btn");
                btnClickProcess();
            }
        }

        private static void captchaType() throws InterruptedException {
            Thread.sleep(300);
            driver.switchTo().defaultContent();
            driver.switchTo().frame(1);
//        String ff = driver.findElement(By.id("imgCaptcha")).getAttribute("src");
            List<WebElement> captchaList = driver.findElements(By.className("validationTxt"));
            WebElement captcha = captchaList.get(0);
            captcha.click();
            WebElement captchaInput = captcha.findElement(By.tagName("input"));
            //captcha 입력 타임아웃
            captcha(captchaInput);
        }

        private static void captcha(WebElement captchaInput) {
            try {
                Thread.sleep(100);
                System.out.println(captchaInput.getAttribute("value").length());
                if (captchaInput.getAttribute("value").length() == 6) {
                    driver.findElements(By.className("capchaBtns")).get(0).findElement(By.tagName("a")).click();
                } else {
                    throw new RuntimeException();
                }
            } catch (RuntimeException e) {
                captcha(captchaInput);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private static Boolean seatSelect() throws InterruptedException {
            try {
                Thread.sleep(100);
                driver.switchTo().defaultContent();
                driver.switchTo().frame(1);
                //328.5
                //맨 끝에 get 일반석 0, 휠체어석 1
                driver.findElements(By.className("list")).get(0).findElements(By.tagName("a")).get(0).click();
                driver.findElements(By.className("twoBtn")).get(0).findElements(By.tagName("a")).get(1).click();

                driver.switchTo().defaultContent();
                driver.switchTo().frame(driver.findElements(By.tagName("iframe")).get(1));
                driver.switchTo().frame(driver.findElements(By.tagName("iframe")).get(0));

                List<WebElement> elSeats = driver.findElements(By.className("stySeat"));
                List<WebElement> elSeatsReformed = new ArrayList<>();
                if (elSeats.size() > 0) {
                    if (byDistance) {
                        // 거리 계산
                        List<Integer> distances = new ArrayList<>();
                        for (WebElement elSeat : elSeats) {
                            distances.add((329 - elSeat.getLocation().getX()) * (329 - elSeat.getLocation().getX()) + (245 - elSeat.getLocation().getY()) * (245 - elSeat.getLocation().getY()));
                        }
//                        // 거리 출력
//                        for (int i = 0; i < elSeats.size(); i++) {
//                            System.out.println(i + " : " + distances.get(i));
//                        }
                        // 거리에 따라 재배열
                        for (int i = 0; i < elSeats.size(); i++) {
                            elSeatsReformed.add(elSeats.get(distances.indexOf(Collections.min(distances))));
                            distances.set(distances.indexOf(Collections.min(distances)), Integer.MAX_VALUE);
                        }
                    }
                    // 좌측좌석부터인 경우, 우측부터 탐색
                    else if (!rightSeat) {
                        for (int i = elSeats.size() - 1; i >= 0; i--) {
                            elSeatsReformed.add(elSeats.get(i));
                        }
                    }
                    // 우측좌석인 경우, 좌측부터 탐색
                    else {
                        elSeatsReformed = elSeats;
                    }
                }

                int checker = 0;
                for (WebElement elSeat : elSeatsReformed) {
                    //레드팀 블루팀 선택에 중요
                    //부등호 '<'는 왼쪽자리 '>'는 오른쪽자리328.5
                    if (rightSeat) {
                        if (elSeat.getLocation().getX() > 328.5) {
                            elSeat.click();
                            checker += 1;

                            if (isTwo){
                                if (checker == 2){
                                    break;
                                }
                            }
                            else {
                                break;
                            }

                        }
                    } else {
                        if (elSeat.getLocation().getX() < 328.5) {
                            elSeat.click();
                            checker += 1;

                            if (isTwo){
                                if (checker == 2){
                                    break;
                                }
                            }
                            else {
                                break;
                            }
                        }
                    }
                }
                // 한장만이라도 건지기, 하나도 선택되지 않은 경우 예외처리
                if (checker < 1) {
                    throw new RuntimeException();
                }
                driver.switchTo().defaultContent();
                driver.switchTo().frame(driver.findElements(By.tagName("iframe")).get(1));
                driver.findElements(By.className("btnWrap")).get(0).findElement(By.tagName("a")).click();
                driver.switchTo().defaultContent();
                return true;
            } catch (IndexOutOfBoundsException e) {
                return false;
            } catch (UnhandledAlertException e) {
                return false;
            } catch (ElementNotInteractableException e) {
                return false;
            } catch (RuntimeException e) {
                return false;
            }
        }

        private static void phase0() {
            driver.switchTo().defaultContent();
            driver.switchTo().frame(0);
            WebElement selEl = driver.findElements(By.className("taL")).get(0).findElement(By.tagName("select"));
            Select select = new Select(selEl);
            int length = select.getOptions().size();
            select.selectByIndex(length - 1);
            driver.switchTo().defaultContent();
            driver.findElement(By.id("SmallNextBtnImage")).click();
            System.out.println("phase0...OK!");
        }

        private static void phase1() {
            driver.switchTo().defaultContent();
            driver.switchTo().frame(driver.findElement(By.id("ifrmBookCertify")));
            WebElement agreeEl = driver.findElement(By.id("Agree"));
            agreeEl.click();
            WebElement agreeEl2 = driver.findElement(By.xpath("//*[@id=\"information\"]/div[2]/a[1]/img"));
            agreeEl2.click();
            driver.switchTo().defaultContent();
            driver.findElement(By.id("SmallNextBtnImage")).click();
            System.out.println("phase1...OK!");
        }

        private static void phase2() throws InterruptedException {
            driver.switchTo().defaultContent();
            driver.switchTo().frame(0);
            Thread.sleep(300);
            WebElement chkEl = driver.findElements(By.className("chk")).get(0);
            chkEl.click();
            //생년월일입력
            driver.findElement(By.id("YYMMDD")).sendKeys(bir);
            driver.switchTo().defaultContent();
            driver.findElement(By.id("SmallNextBtnImage")).click();
            System.out.println("phase2...OK!");
        }

        private static void phase3() throws InterruptedException {
            driver.switchTo().defaultContent();
            driver.switchTo().frame(0);
            Thread.sleep(300);
            WebElement chkEl2 = driver.findElements(By.className("chk")).get(3);
            chkEl2.click();
            driver.switchTo().defaultContent();
            driver.findElement(By.id("SmallNextBtnImage")).click();
            System.out.println("phase3...OK!");
        }

        private static void phase4() throws InterruptedException {
            driver.switchTo().defaultContent();
            driver.switchTo().frame(0);
            Thread.sleep(300);
            WebElement chkEl3 = driver.findElement(By.id("checkAll"));
            chkEl3.click();
            driver.switchTo().defaultContent();
            driver.findElement(By.id("LargeNextBtn")).click();
            System.out.println("phase4...OK!");
        }
    }

}

