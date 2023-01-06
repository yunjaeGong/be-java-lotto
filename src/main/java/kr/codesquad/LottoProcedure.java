package kr.codesquad;

import kr.codesquad.sequence.LottosGenerator;
import kr.codesquad.sequence.ManualSequenceGenerator;
import kr.codesquad.sequence.ShuffleSequenceGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LottoProcedure {

    final static Scanner sc = new Scanner(System.in);

    private Lotto winningLotto;  // 실제 로또 번호

    private List<Lotto> lottos;

    private Money money;

    private int bonus;

    private int ticketsLeftToGenerate;  // 생성할 로또 티켓 수

    private int manualCnt;  // 수동으로 구매할 로또 번호의 수
    private int shuffleCnt;  // 자동으로 구매할 로또 번호의 수

    public LottoProcedure() {
        this.lottos = new ArrayList<>();
    }

    public void run() {
        final int MANUAL = -1;
        // 구매 금액 입력
        takeMoney();

        takeManualLottoCount();

        generateLottos(new ManualSequenceGenerator(), this.manualCnt);

        this.shuffleCnt = this.ticketsLeftToGenerate;
        // 로또 번호 생성
        generateLottos(new ShuffleSequenceGenerator(), this.shuffleCnt);

        printLottoSequence();

        takeActualInput();  // 로또 번호, 보너스 번호 입력

        LottoResult result = new LottoResult(this);
        result.showResults();
    }

    private void takeMoney() {
        // 구입 금액 입려

        System.out.println("구입금액을 입력해 주세요.");
        int m = sc.nextInt();  // 구입금액 입력

        this.money = new Money(m);

        this.ticketsLeftToGenerate = money.numOfTickets;  // 생성할 티켓 수
    }

    private void takeManualLottoCount() {
        // 수동으로 구매할 로또 처리
        System.out.println("\n수동으로 구매할 로또 수를 입력해 주세요.");
        this.manualCnt = sc.nextInt();  // 수동으로 구매할 로또 번호 수 입력
        sc.nextLine();
    }

    private void generateLottos(LottosGenerator generator, int ticketsToGenerate) {
        List<Lotto> lottos = generator.generate(ticketsToGenerate);
        this.lottos.addAll(lottos);

        issueLottos(ticketsToGenerate);
    }

    private void printLottoSequence() {
        System.out.println("\n수동으로 " + this.manualCnt + "장, 자동으로 " + this.shuffleCnt + " 개를 구매했습니다.");

        // 구매한 로또 번호 출력
        // TODO: 이 클래스의 메서드로
        for(Lotto lotto : this.lottos) {
            System.out.println(lotto);
        }
    }

    private void takeActualInput() {
        this.shuffleCnt = ticketsLeftToGenerate;  // 자동으로 구매할 로또 수

        System.out.println("\n지난 주 당첨 번호를 입력해 주세요.");
        String str = sc.nextLine();  // 당첨번호 입력

        // 당첨 번호처리, 반환
        this.winningLotto = new WinningLotto(parseCommaSeparatedLineInput(str), bonus);

        System.out.println("보너스 볼을 입력해 주세요.");
        this.bonus = sc.nextInt();  // 보너스 번호 입력
    }

    public List<List<Integer>> takeManualInput() {
        List<List<Integer>> numbers = new ArrayList<>();
        // 수동으로 구매할 로또 처리
        System.out.println("\n수동으로 구매할 로또 수를 입력해 주세요.");
        this.manualCnt = sc.nextInt();  // 수동으로 구매할 로또 번호 수 입력
        sc.nextLine();

        System.out.println("\n수동으로 구매할 번호를 입력해 주세요.");

        for(int i=0;i<this.manualCnt;++i) {
            String str = sc.nextLine();
            numbers.add(parseCommaSeparatedLineInput(str));  // 구매한 로또 번호
        }

        return numbers;
    }

    public static List<Integer> parseCommaSeparatedLineInput(String str) {
        return Stream.of(str.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .sorted()
                .collect(Collectors.toList());
    }

    private void issueLottos(int lottoCnt) {
        if (this.ticketsLeftToGenerate < lottoCnt)
            throw new IllegalArgumentException("구매할 수 있는 로또보다 구매하고자 하는 로또가 더 많습니다");

        this.ticketsLeftToGenerate -= lottoCnt;
    }

    public Lotto getWinningLotto() {
        return winningLotto;
    }

    public List<Lotto> getLottos() {
        return lottos;
    }

    public Money getMoney() {
        return money;
    }
}
