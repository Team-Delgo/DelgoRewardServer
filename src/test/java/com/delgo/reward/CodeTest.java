package com.delgo.reward;


import com.delgo.reward.domain.code.Code;
import com.delgo.reward.service.CodeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CodeTest {

    @Autowired
    private CodeService codeService;

    @Test
    public void getGeoCodeFromDBTest() {
        //given
        String SIGUGUN = "송파구";

        //when
        Code code = new Code();

        System.out.println("geoCode : " + code.getCode());
        System.out.println("p_geoCode : " + code.getPCode());
        //then
        assertNotNull(code);
    }

    @Test
    public void saveBreedCodeTest() {
        //given
        String breeds = "말티즈 (Maltese)\n" +
                "시츄 (Shih Tzu)\n" +
                "시고르자브종 (Sigorzav)\n" +
                "믹스 (Mix)\n" +
                "푸들 (Poodle)\n" +
                "불독 (Bulldog)\n" +
                "비숑 프리제 (Bichon Frise)\n" +
                "요크셔 테리어 (Yorkshire Terrier)\n" +
                "진돗개 (Jindo)\n" +
                "시바견 (Shiba Inu)\n" +
                "웰시 코기 (Welsh Corgi)\n" +
                "라브라도 리트리버 (Labrador Retriever)\n" +
                "닥스훈트 (Dachshund)\n" +
                "그레이 하운드 (Greyhound)\n" +
                "치와와 (Chihuahua)\n" +
                "스피츠 (Spitz)\n" +
                "사모예드 (Samoyed)\n" +
                "골든 리트리버 (Golden Retriever)\n" +
                "샤페이 (Shar Pei)\n" +
                "마리노아 (Marinoa)\n" +
                "말라뮤트 (Alaskan Malamute)\n" +
                "아메리칸 불리 (American Bully)\n" +
                "셔틀랜드 쉽독 (Shetland Sheepdog)\n" +
                "비글 (Beagle)\n" +
                "보더 콜리 (Border Collie)\n" +
                "도베르만 (Doberman)\n" +
                "페키니즈 (Pekingese)\n" +
                "보스턴 테리어 (Boston Terrier)\n" +
                "아이리시 세터 (Irish Setter)\n" +
                "보르조이 (Borzoi)\n" +
                "아키타 (Akita)\n" +
                "플랫코티드 레트리버 (Flat-coated Retriever)\n" +
                "와이어 폭스테리어 (Wire Fox Terrier)\n" +
                "코카 스파니엘 (Cocker Spaniel)\n" +
                "뉴펀들랜드 (Newfoundland)\n" +
                "보르도 마스티프 (Bordeaux Mastiff)\n" +
                "로트와일러 (Rottweiler)\n" +
                "저먼 셰퍼드 (German Shepherd)\n" +
                "잭 러셀 테리어 (Jack Russell Terrier)\n" +
                "이탈리안 그레이하운드 (Italian Greyhound)\n" +
                "아펜핀셔 (Affinpincher)\n" +
                "테리어 (Terrier)\n" +
                "풍산개 (Pungsan Dog)\n" +
                "사냥개 (Hunting Dog)\n" +
                "레드본 쿤하운드 (Redbone Coonhound)\n" +
                "시베리안 허스키 (Siberian Husky)\n" +
                "아이리시 울프하운드 (Irish Wolfhound)\n" +
                "폴드 (Fold)\n" +
                "엘크하운드 (Elkhound)\n" +
                "불리코리아노 (Bully Kutta)\n" +
                "체서피크 베이 리트리버 (Chesapeake Bay Retriever)\n" +
                "레온베르거 (Leonberger)\n" +
                "카네코르소 (Cane Corso)\n" +
                "달마시안 (Dalmatian)\n" +
                "흑토종 (Korean Jindo Dog)\n" +
                "미니어처 핀셔 (Miniature Pinscher)\n" +
                "사물개 (Samo)\n" +
                "미니어처 슈나우저 (Miniature Schnauzer)\n" +
                "벨지안 말리노이즈 (Belgian Malinois)\n" +
                "토이 푸들 (Toy Poodle)\n" +
                "블랙 러시안 테리어 (Black Russian Terrier)\n" +
                "올드 잉글리시 쉽독 (Old English Sheepdog)\n" +
                "샤이 푸 (Shih Poo)\n" +
                "바셋 하운드 (Basset Hound)\n" +
                "포메라니안 (Pomeranian)\n" +
                "브리타니 스파니엘 (Brittany Spaniel)\n" +
                "빠삐용 (Papillon)\n" +
                "플레인즈 불리 (Plains Bulldog)\n" +
                "잉글리시 코커 스파니엘 (English Cocker Spaniel)\n" +
                "비숑 마르티스 (Bichon Maltese)\n" +
                "아시안 리트리버 (Asian Retriever)\n" +
                "스태퍼드셔 불 테리어 (Staffordshire Bull Terrier)\n" +
                "퍼그 (Pug)\n" +
                "브리어드 (Briard)\n" +
                "마스티프 (Mastiff)\n" +
                "오스트레일리안 셰퍼드 (Australian Shepherd)\n" +
                "크로스비 글랜더 (Crosby-Glendower)\n" +
                "도고 아르헨티노 (Dogo Argentino)\n" +
                "스무살 (Smooth Collie)\n" +
                "레브라도 불독 (Labradoodle)\n" +
                "뉴욕셔 테리어 (Yorkshire Terrier)\n" +
                "케언 테리어 (Cairn Terrier)\n" +
                "키즈현 (Kishu Ken)\n" +
                "토이 푸 (Toy Poodle)\n" +
                "흑백진돗개 (Black and Tan Jindo)\n" +
                "엉봉 (Mutt)\n" +
                "카발리에 킹 찰스 스패니얼 (Cavalier King Charles Spaniel)\n" +
                "웨스트 하이랜드 화이트 테리어 (West Highland White Terrier)\n" +
                "레이크랜드 테리어 (Lakeland Terrier)\n" +
                "토이 폭스 테리어 (Toy Fox Terrier)\n" +
                "와이어 헤어드 포인팅 그리폰 (Wirehaired Pointing Griffon)\n" +
                "티베탄 스파니얼 (Tibetan Spaniel)\n" +
                "보더 테리어 (Border Terrier)\n" +
                "코르기 (Corgi)\n" +
                "마르티즈 (Maltitz)\n" +
                "노르웨이언 엘크하운드 (Norwegian Elkhound)\n" +
                "그레이트 데인 (Great Dane)\n" +
                "아메리칸 스태퍼드셔 테리어 (American Staffordshire Terrier)\n" +
                "불 타입 (Bull Terrier)\n" +
                "아이리시 테리어 (Irish Terrier)\n" +
                "잭 러셀 테리어 (Jack Russell Terrier)\n" +
                "카바트진 (Cavachon)\n" +
                "시베리안 라이카 (Siberian Laika)\n" +
                "미니어처 핀치 (Miniature Pinscher)\n" +
                "샤틀랜드 쉽독 (Shetland Sheepdog)\n" +
                "아프간 하운드 (Afghan Hound)\n" +
                "캐벨리에 킹 찰스 스패니얼 (Cavalier King Charles Spaniel)\n" +
                "퍼시아 레온 (Persian Greyhound)\n" +
                "미니어처 슈나우져 (Miniature Schnauzer)\n" +
                "블러드하운드 (Bloodhound)\n" +
                "바셋 헌트 (Basset Hound)\n" +
                "카이거 (Keeshond)\n" +
                "풀리 (Puli)\n" +
                "푸미 (Pumi)\n" +
                "페럿 (Parrot)\n" +
                "불도그 아르젠티노 (Dogo Argentino)\n" +
                "오지 (Ozzy)\n" +
                "토이 푸들 (Toy Poodle)\n" +
                "프렌치 불도그 (French Bulldog)\n" +
                "노르딕 스패니얼 (Nordic Spaniel)\n" +
                "오스트레일리언 티루비 (Australian Terrier)\n" +
                "소프트 코티드 휘튼 테리어 (Soft Coated Wheaten Terrier)\n" +
                "마스티노 나폴리탄 (Mastino Napoletano)\n" +
                "흰색 스위스 셰퍼드 (White Swiss Shepherd)\n" +
                "미니어처 불독 (Miniature Bulldog)\n" +
                "도살개 (Dosa Gae)\n" +
                "토이 폭스테리어 (Toy Fox Terrier)\n" +
                "체코슬로바키안 보헤미안 (Czechoslovakian Wolfdog)\n" +
                "코리안 조이 독 (Korean Jindo Dog)\n" +
                "마운틴 카라반 (Mountain Cur)\n" +
                "바르마즈 (Barbamas)\n" +
                "미디움 슈나우저 (Medium Schnauzer)\n" +
                "카틀 독 (Cattle Dog)\n" +
                "포르투갈 워터 독 (Portuguese Water Dog)\n" +
                "미니어처 스태퍼드셔 불 테리어 (Miniature Staffordshire Bull Terrier)\n" +
                "미니어처 골든도드 (Miniature Goldendoodle)\n" +
                "알레스칸 케스키 (Alaskan Klee Kai)\n" +
                "스패니쉬 마스티프 (Spanish Mastiff)\n" +
                "진돗개 혼합종 (Mixed Breed Jindo)\n" +
                "소말리 (Somali)\n" +
                "레이크랜드 테리어 (Lakeland Terrier)\n" +
                "아프리카 대륙 거주 개 (African Village Dog)\n" +
                "맨체스터 테리어 (Manchester Terrier)\n" +
                "시베리안 하스키 (Siberian Husky)\n" +
                "티베탄 마스티프 (Tibetan Mastiff)\n" +
                "불 피보 (Bull Pei)\n" +
                "박스웰리 테리어 (Boxer Terrier)\n" +
                "블루 힐러 (Blue Heeler)\n" +
                "블랙 몰리노이즈 (Black Molossus)\n" +
                "브리트니 스파니엘 혼합종 (Mixed Breed Brittany Spaniel)\n" +
                "샤크독 (Shar Pei and Akita mix)\n" +
                "잉글리시 스프링어 스파니엘 (English Springer Spaniel)\n" +
                "캔디드 불리 (Candid Bulldog)\n" +
                "미니어처 다이너 핀셔 (Miniature Dachshund)\n" +
                "미니어처 셔나즈 (Miniature Schnauzer)\n" +
                "스무살 (Smooth Collie)\n" +
                "로드시안 릿지백 (Rhodesian Ridgeback)\n" +
                "아프간 하운드 (Afghan Hound)\n" +
                "포인터 (Pointer)\n" +
                "보더 콜리 (Border Collie)\n" +
                "불리 코리아노 (Bully Kutta)\n" +
                "페키니즈 (Pekingese)\n" +
                "비글 (Beagle)\n" +
                "도베르만 핀셔 (Doberman Pinscher)\n" +
                "진도개 (Jindo)\n" +
                "시바견 (Shiba Inu)\n" +
                "아이리시 세터 (Irish Setter)\n" +
                "미니어처 푸들 (Miniature Poodle)\n" +
                "시바 이누 혼합종 (Shiba Inu mix)\n" +
                "불독 (Bulldog)\n" +
                "오스트레일리안 셰퍼드 (Australian Shepherd)\n" +
                "사모예드 (Samoyed)\n" +
                "위펫 (Whippet)\n" +
                "치와와 (Chihuahua)\n" +
                "노르웨이언 블루 엘크하운드 (Norwegian Blue Elk Hound)\n" +
                "잉글리시 불도그 (English Bulldog)\n" +
                "라사압소 (Lhasa Apso)\n" +
                "불리 테리어 (Bull Terrier)\n" +
                "저먼 와이어헤어드 포인터 (German Wirehaired Pointer)\n" +
                "샤틀랜드 쉽독 혼합종 (Mixed Breed Shetland Sheepdog)\n" +
                "올드 잉글리시 불독 (Old English Bulldog)\n" +
                "캐리 블루 테리어 (Kerry Blue Terrier)\n" +
                "로저 더 로지스타 (Rogers the Lodger)\n" +
                "아키타이누 (Akita Inu)\n" +
                "알래스칸 말라뮤트 (Alaskan Malamute)\n" +
                "아메리칸 아키다 (American Akita)\n" +
                "아메리칸 불도그 (American Bulldog)\n" +
                "아메리칸 퍼스트 (American First)\n" +
                "아메리칸 헤어리 (American Hairless Terrier)\n" +
                "아메리칸 피트불 테리어 (American Pitbull Terrier)\n" +
                "아메리칸 스태퍼드셔 불 테리어 (American Staffordshire Bull Terrier)\n" +
                "아스트랄리안 캐틀 독 (Australian Cattle Dog)\n" +
                "아스트랄리안 셰퍼드 (Australian Shepherd)\n" +
                "오스트레일리안 실키 테리어 (Australian Silky Terrier)\n" +
                "오스트레일리안 테리어 (Australian Terrier)\n" +
                "바셋 프리에 (Basset Fauve de Bretagne)\n" +
                "벨지안 테뷰런 (Belgian Tervuren)\n" +
                "베들링턴 테리어 (Bedlington Terrier)\n" +
                "보더 푸들 (Borderpoo)\n" +
                "보르조이형 (Borzoi)\n" +
                "브리덜리온 (Briard)\n" +
                "버니즈 마운틴 독 (Bernese Mountain Dog)\n" +
                "카난 도그 (Canan Dog)\n" +
                "캐밀라 (Cammilla)\n" +
                "세티 (Cesky Terrier)\n" +
                "코르카 디페로 (Cirneco dell'Etna)\n" +
                "컬리 (Curly Coated Retriever)\n" +
                "케리 블루 테리어 (Kerry Blue Terrier)\n" +
                "크롬폰트 카렐리안 베어 독 (Kromfohrlander)\n" +
                "라포니안 허딩 드그 (Lapponian Herder)\n" +
                "로웨노 (Lowchen)\n" +
                "미니어처 에어데일 테리어 (Miniature Airedale Terrier)\n" +
                "노르포크 테리어 (Norfolk Terrier)\n" +
                "오스트레일리안 켈피 (Australian Kelpie)\n" +
                "플랫페이스드 레트리버 (Flat-Coated Retriever)\n" +
                "벨지안 테뷰런 (Belgian Tervuren)\n" +
                "브루셀 그리펀 (Brussels Griffon)\n" +
                "말티푸 (Maltipoo)\n" +
                "폭스테리어 (Fox Terrier)\n" +
                "바셋 블러드하운드 (Basset Bloodhound)\n" +
                "에어데일 테리어 (Airedale Terrier)\n" +
                "카보치아 (Cavachia)\n" +
                "페키니즈 (Pekingese)\n" +
                "그레이트 피레니즈 (Great Pyrenees)\n" +
                "푸미코 (Poomi-Co)\n" +
                "벨지안 라이켄와 (Belgian Laekenois)\n" +
                "저먼 쇼트헤어드 포인터 (German Shorthaired Pointer)\n" +
                "블루틱 쿤하운드 (Bluetick Coonhound)\n" +
                "아이리시 워터 스파니얼 (Irish Water Spaniel)\n" +
                "뉴질랜드 헌트웨이 (New Zealand Huntaway)\n" +
                "카리 포니안 (Carry Ponian)\n" +
                "보스턴 부울 (Boston Bull)\n" +
                "프렌치 스패니얼 (French Spaniel)\n" +
                "아메리칸 불독 (American Bulldog)\n" +
                "미니어처 아메리칸 에스키모 (Miniature American Eskimo)\n" +
                "올드 잉글리쉬 불독 (Old English Bulldog)\n" +
                "미니어처 비글 (Miniature Beagle)\n" +
                "코몬도르 (Komondor)\n" +
                "엘키타 (Elkhound)\n" +
                "미니어처 아이리시 울프하운드 (Miniature Irish Wolfhound)\n" +
                "캐나디언 에스키모 도그 (Canadian Eskimo Dog)\n" +
                "그레이트 스위스 마운틴 도그 (Great Swiss Mountain Dog)\n" +
                "에어데일 테리어 (Airedale Terrier)\n" +
                "아메리칸 아키다 (American Akita)\n" +
                "베들링턴 테리어 (Bedlington Terrier)\n" +
                "흰색 테리어 (Westie)\n" +
                "보더 카오스 헤어 (Border Collie X Australian Cattle Dog)\n" +
                "블랙 탄 쿤하운드 (Black and Tan Coonhound)\n" +
                "블루 타이 쿤하운드 (Bluetick Coonhound)\n" +
                "보더 블러드하운드 (Bloodhound X Border Collie)\n" +
                "보더 포인터 (Border Pointer)\n" +
                "보더-스피츠 (Border Spitz)\n" +
                "보더-이탈리안 그레이하운드 (Border-Italian Greyhound)\n" +
                "보르조이 혼합종 (Borzoi Mix)\n" +
                "보스턴 불독 (Boston Bulldog)\n" +
                "브리베이션 (Brievier)\n" +
                "브리티쉬 불독 (British Bulldog)\n" +
                "브라질리언 보르조이 (Brazilian Borzoi)\n" +
                "불교적 불리 (Buddhist Bulldog)\n" +
                "불랙탄 캔코로소 (Blacktan Can Corso)\n" +
                "불랙탄 마스티프 (Blacktan Mastiff)\n" +
                "불랙 탄 푸들 (Black and Tan Poodle)\n" +
                "불랙 탄 프렌치 불도그 (Black and Tan French Bulldog)\n" +
                "불랙 탄 올드 잉글리시 불독 (Black and Tan Old English Bulldog)\n" +
                "불랙탄 포메라니안 (Black and Tan Pomeranian)\n" +
                "불타입 말티푸 (Bull Terrier x Maltese)\n" +
                "불타입 불독-올드 잉글리시 불독 혼합종 (Bully-Olde English Bulldog Mix)\n" +
                "버니즈 마운틴 독 (Bernese Mountain Dog)\n" +
                "카타하울라 리트리버 (Catahoula Retriever)\n" +
                "카발리에 스프니엘 혼합종 (Cavalier Spaniel Mix)\n" +
                "코카푸 (Cockapoo)\n" +
                "허스키 라브라도 (Huskador)\n" +
                "불독 블랙 브린들 (Black Brindle Bulldog)\n" +
                "빅시 (Beauceron)\n" +
                "토이 맨체스터 테리어 (Toy Manchester Terrier)\n" +
                "카타하울라 리트리버 (Catahoula Retriever)\n" +
                "아메리칸 에스키모 도그 (American Eskimo Dog)\n" +
                "스탠다드 슈나우저 (Standard Schnauzer)\n" +
                "미니어처 콜리 (Miniature Collie)\n" +
                "스페인 워터 독 (Spanish Water Dog)\n" +
                "버니즈 마운틴 독 (Bernese Mountain Dog)\n" +
                "아메리칸 카커 스파니얼 (American Cocker Spaniel)\n" +
                "마운틴 피클 (Mountain Feist)\n" +
                "페퍼민트 스파니얼 (Peppermint Spaniel)\n" +
                "마운틴 블랙 마우스 마운틴 독 (Mountain Black Mouth Cur)\n" +
                "보더 포인터 (Border Pointer)\n" +
                "독일 와이어헤어드 포인터 (German Wirehaired Pointer)\n" +
                "포인트러 (Pointador)\n" +
                "풀리 블랙 몰리노이즈 (Puli Black Molossus)\n" +
                "아펜핀셔 (Affenpinscher)\n" +
                "크로아티안 셰퍼드 (Croatian Shepherd)\n" +
                "블루 틱 쿤하운드 (Blue Tick Coonhound)\n" +
                "풀리 블랙 몰리노이즈 (Puli Black Molossus)\n" +
                "엘레그란티스 사파이로 (Elegantissima Saphiro)\n" +
                "헝가리안 비즐라 (Hungarian Vizsla)\n" +
                "허크불 (Herquebus)\n" +
                "오스트레일리안 실포인터 (Australian Silky Terrier)\n" +
                "미디움 골든도드 (Medium Goldendoodle)\n" +
                "크롬포 (Cromfo)\n" +
                "카타리나 독 (Catalina Dog)\n" +
                "브리티시 불독 (British Bulldog)\n" +
                "잉글리시 코커 스패니얼 (English Cocker Spaniel)\n" +
                "미니어처 스무스 헤어 다찌 (Miniature Smooth Haired Dachshund)\n" +
                "포메라니안 푸피 (Pomeranian Poochy)\n" +
                "아메리칸 에스키모 (American Eskimo)\n" +
                "코리안 무스독 (Korean Moose Dog)\n" +
                "블랙마우스 케릭 (Black Mouth Cur)\n" +
                "크롤러 스패니얼 (Crawly Spaniel)\n" +
                "미니어처 웰시 코기 (Miniature Welsh Corgi)\n" +
                "폭스테리어 (Fox Terrier)\n" +
                "그레이트 피레니즈 (Great Pyrenees)\n" +
                "진돗개 혼합종 (Mixed Breed Jindo Dog)\n" +
                "맨체스터 테리어 (Manchester Terrier)\n" +
                "폴리시 로웰랜드 시프도그 (Polish Lowland Sheepdog)\n" +
                "미니어처 불도그 (Miniature Bulldog)\n" +
                "카타하울라 레온 (Catahoula Leopard Dog)\n" +
                "크로아티안 쉽독 (Croatian Sheepdog)\n" +
                "셔틀랜드 피니쉬 스패니얼 (Shetland Finnish Spaniel)\n" +
                "헝가리언 비즐라 (Hungarian Vizsla)\n" +
                "빠삐용 혼합종 (Mixed Breed Papillon)\n" +
                "크롤러 혼합종 (Mixed Breed Crawler)\n" +
                "잉글리시 폭스하운드 (English Foxhound)\n" +
                "박스러너 (Boxrunner)\n" +
                "라프템 (Laptem)\n" +
                "아시아틱 리트리버 (Asian Retriever)\n" +
                "맥시칸 헤어리스 도그 (Mexican Hairless Dog)\n" +
                "브리덜벡 그리폰 (Brussels Griffon)\n" +
                "사바나 (Savannah)\n" +
                "살루키 (Saluki)\n" +
                "세인트 버나드 (Saint Bernard)\n";

        String[] breed_array = breeds.split("\n");

        int i = 1 ;
        for(String breed : breed_array) {
            Code code = new Code();
            breed = breed.substring(0, breed.length() - 1);
            String[] breed_split = breed.split( "\\(");
//            System.out.println("한글 이름 : " + breed_split[0] + " 영어 이름 : " + breed_split[1]);

            String codeDt;
            if(i < 10)
                codeDt = "DT000" + i++;
            else if(i < 100)
                codeDt = "DT00" + i++;
            else
                codeDt = "DT0" + i++;

            code.setCode(codeDt);
            code.setPCode("");
            code.setType("breed");
            code.setCodeName(breed_split[0]);
            code.setCodeDesc(breed_split[1]);

            System.out.println("code : " + code);
            if(codeService.checkDuplicateBreedCode(breed_split[0])){
                System.out.println(breed_split[0] + " 이미 있는 품종입니다.");
                i--;
            } else {
                codeService.register(code);
            }
        }

        //when


        //then
    }
}