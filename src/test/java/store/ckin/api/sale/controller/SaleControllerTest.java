package store.ckin.api.sale.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import store.ckin.api.booksale.dto.request.BookSaleCreateRequestDto;
import store.ckin.api.booksale.dto.response.BookAndBookSaleResponseDto;
import store.ckin.api.booksale.dto.response.BookSaleResponseDto;
import store.ckin.api.common.domain.PageInfo;
import store.ckin.api.common.dto.PagedResponse;
import store.ckin.api.payment.dto.response.PaymentResponseDto;
import store.ckin.api.payment.entity.PaymentStatus;
import store.ckin.api.sale.dto.request.SaleCreateRequestDto;
import store.ckin.api.sale.dto.request.SaleDeliveryUpdateRequestDto;
import store.ckin.api.sale.dto.response.SaleCheckResponseDto;
import store.ckin.api.sale.dto.response.SaleDetailResponseDto;
import store.ckin.api.sale.dto.response.SaleInfoResponseDto;
import store.ckin.api.sale.dto.response.SaleResponseDto;
import store.ckin.api.sale.dto.response.SaleWithBookResponseDto;
import store.ckin.api.sale.entity.DeliveryStatus;
import store.ckin.api.sale.entity.SalePaymentStatus;
import store.ckin.api.sale.facade.SaleFacade;

/**
 * 주문 컨트롤러 테스트.
 *
 * @author 정승조
 * @version 2024. 03. 07.
 */

@AutoConfigureRestDocs(uriHost = "133.186.247.149", uriPort = 7030)
@WebMvcTest(SaleController.class)
class SaleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SaleFacade saleFacade;

    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    @DisplayName("주문 등록 테스트")
    void testCreateSale() throws Exception {

        given(saleFacade.createSale(any(SaleCreateRequestDto.class)))
                .willReturn("12314");

        BookSaleCreateRequestDto bookSale = new BookSaleCreateRequestDto();
        ReflectionTestUtils.setField(bookSale, "bookId", 1L);
        ReflectionTestUtils.setField(bookSale, "appliedCouponId", 1L);
        ReflectionTestUtils.setField(bookSale, "packagingId", 4L);
        ReflectionTestUtils.setField(bookSale, "quantity", 2);
        ReflectionTestUtils.setField(bookSale, "paymentAmount", 10000);


        SaleCreateRequestDto requestDto = new SaleCreateRequestDto();
        ReflectionTestUtils.setField(requestDto, "bookSaleList", List.of(bookSale));
        ReflectionTestUtils.setField(requestDto, "memberId", 1L);
        ReflectionTestUtils.setField(requestDto, "saleTitle", "테스트 책");
        ReflectionTestUtils.setField(requestDto, "saleOrdererName", "정승조");
        ReflectionTestUtils.setField(requestDto, "saleOrdererContact", "01012345678");
        ReflectionTestUtils.setField(requestDto, "saleReceiverName", "정승조");
        ReflectionTestUtils.setField(requestDto, "saleReceiverContact", "01012345678");
        ReflectionTestUtils.setField(requestDto, "deliveryFee", 3000);
        ReflectionTestUtils.setField(requestDto, "saleDeliveryDate", LocalDate.of(2024, 3, 7));
        ReflectionTestUtils.setField(requestDto, "postcode", "123456");
        ReflectionTestUtils.setField(requestDto, "address", "광주광역시 동구 조선대 5길 ");
        ReflectionTestUtils.setField(requestDto, "detailAddress", "1층 NHN");
        ReflectionTestUtils.setField(requestDto, "pointUsage", 0);
        ReflectionTestUtils.setField(requestDto, "totalPrice", 10000);

        String json = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().json("12314"))
                .andDo(document("sale/createSale/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("bookSaleList[].bookId").description("도서 ID"),
                                fieldWithPath("bookSaleList[].appliedCouponId").description("적용된 쿠폰 ID"),
                                fieldWithPath("bookSaleList[].packagingId").description("포장 ID"),
                                fieldWithPath("bookSaleList[].quantity").description("도서 수량"),
                                fieldWithPath("bookSaleList[].paymentAmount").description("도서 결제 금액"),
                                fieldWithPath("memberId").description("주문하는 회원 ID").optional(),
                                fieldWithPath("saleTitle").description("주문 제목 (책 제목 + 수량)"),
                                fieldWithPath("saleOrdererName").description("주문자 이름"),
                                fieldWithPath("saleOrdererContact").description("주문자 연락처"),
                                fieldWithPath("saleReceiverName").description("수령자 이름"),
                                fieldWithPath("saleReceiverContact").description("수령자 연락처"),
                                fieldWithPath("deliveryFee").description("배송비"),
                                fieldWithPath("saleDeliveryDate").description("배송 날짜"),
                                fieldWithPath("postcode").description("우편번호"),
                                fieldWithPath("address").description("주소"),
                                fieldWithPath("detailAddress").description("상세주소"),
                                fieldWithPath("pointUsage").description("포인트 사용량"),
                                fieldWithPath("totalPrice").description("총 주문 금액")
                        )
                ));

        verify(saleFacade, times(1)).createSale(any());
    }

    @Test
    @DisplayName("주문 목록 조회 테스트")
    void testGetSales() throws Exception {
        SaleResponseDto responseDto =
                new SaleResponseDto(
                        1L,
                        1L,
                        "테스트 제목",
                        "test@test.com",
                        "1234",
                        "정승조",
                        "01012345678",
                        "정승조",
                        "01012345678",
                        "광주광역시 동구 조선대 5길",
                        LocalDateTime.of(2024, 3, 7, 12, 0, 0),
                        LocalDateTime.of(2024, 3, 7, 12, 0, 0).plusDays(1),
                        LocalDate.of(2024, 3, 7).plusDays(3),
                        DeliveryStatus.READY,
                        3000,
                        0,
                        10000,
                        SalePaymentStatus.WAITING,
                        "123456"
                );

        PageInfo pageInfo = new PageInfo(1, 10, 1, 1);
        PagedResponse<List<SaleResponseDto>> pagedResponse =
                new PagedResponse<>(List.of(responseDto), pageInfo);

        given(saleFacade.getSales(any()))
                .willReturn(pagedResponse);

        mockMvc.perform(get("/api/sales")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.data[0].saleId").value(responseDto.getSaleId()),
                        jsonPath("$.data[0].memberEmail").value(responseDto.getMemberEmail()),
                        jsonPath("$.data[0].saleNumber").value(responseDto.getSaleNumber()),
                        jsonPath("$.data[0].saleOrdererName").value(responseDto.getSaleOrdererName()),
                        jsonPath("$.data[0].saleOrdererContact").value(responseDto.getSaleOrdererContact()),
                        jsonPath("$.data[0].saleReceiverName").value(responseDto.getSaleReceiverName()),
                        jsonPath("$.data[0].saleReceiverContact").value(responseDto.getSaleReceiverContact()),
                        jsonPath("$.data[0].saleReceiverAddress").value(responseDto.getSaleReceiverAddress()),
                        jsonPath("$.data[0].saleShippingDate").isNotEmpty(),
                        jsonPath("$.data[0].saleDeliveryDate").isNotEmpty(),
                        jsonPath("$.data[0].saleDeliveryStatus").value(responseDto.getSaleDeliveryStatus().name()),
                        jsonPath("$.data[0].salePointUsage").value(responseDto.getSalePointUsage()),
                        jsonPath("$.data[0].saleTotalPrice").value(responseDto.getSaleTotalPrice()),
                        jsonPath("$.data[0].salePaymentStatus").value(responseDto.getSalePaymentStatus().name()),
                        jsonPath("$.data[0].saleShippingPostCode").value(responseDto.getSaleShippingPostCode()),
                        jsonPath("$.pageInfo.page").value(pageInfo.getPage()),
                        jsonPath("$.pageInfo.size").value(pageInfo.getSize()),
                        jsonPath("$.pageInfo.totalElements").value(pageInfo.getTotalElements()),
                        jsonPath("$.pageInfo.totalPages").value(pageInfo.getTotalPages()))
                .andDo(document("sale/getSaleList/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 크기")
                        ),
                        responseFields(
                                fieldWithPath("data[].saleId").description("주문 ID"),
                                fieldWithPath("data[].memberId").description("회원 ID"),
                                fieldWithPath("data[].title").description("주문명"),
                                fieldWithPath("data[].memberEmail").description("회원 이메일"),
                                fieldWithPath("data[].saleNumber").description("주문 번호"),
                                fieldWithPath("data[].saleOrdererName").description("주문자 이름"),
                                fieldWithPath("data[].saleOrdererContact").description("주문자 연락처"),
                                fieldWithPath("data[].saleReceiverName").description("수령자 이름"),
                                fieldWithPath("data[].saleReceiverContact").description("수령자 연락처"),
                                fieldWithPath("data[].saleReceiverAddress").description("배송지"),
                                fieldWithPath("data[].saleDate").description("주문 일자"),
                                fieldWithPath("data[].saleShippingDate").description("출고 일자"),
                                fieldWithPath("data[].saleDeliveryDate").description("배송 일자"),
                                fieldWithPath("data[].saleDeliveryStatus").description("배송 상태"),
                                fieldWithPath("data[].saleDeliveryFee").description("배송비"),
                                fieldWithPath("data[].salePointUsage").description("포인트 사용량"),
                                fieldWithPath("data[].saleTotalPrice").description("총 주문 금액"),
                                fieldWithPath("data[].salePaymentStatus").description("결제 상태"),
                                fieldWithPath("data[].saleShippingPostCode").description("배송지 우편번호"),
                                fieldWithPath("pageInfo.page").description("페이지 번호"),
                                fieldWithPath("pageInfo.size").description("페이지 크기"),
                                fieldWithPath("pageInfo.totalElements").description("총 주문 수"),
                                fieldWithPath("pageInfo.totalPages").description("총 페이지 수")
                        )
                ));

        verify(saleFacade, times(1)).getSales(any());
    }

    @Test
    @DisplayName("주문 ID로 주문 상세 조회 - 관리자 페이지")
    void testGetSaleDetail() throws Exception {
        SaleResponseDto sale =
                new SaleResponseDto(
                        1L,
                        1L,
                        "테스트 제목",
                        "test@test.com",
                        "1234",
                        "정승조",
                        "01012345678",
                        "정승조",
                        "01012345678",
                        "광주광역시 동구 조선대 5길",
                        LocalDateTime.of(2024, 3, 7, 12, 0, 0),
                        LocalDateTime.of(2024, 3, 7, 12, 0, 0).plusDays(1),
                        LocalDate.of(2024, 3, 7).plusDays(3),
                        DeliveryStatus.READY,
                        3000,
                        0,
                        10000,
                        SalePaymentStatus.WAITING,
                        "123456"
                );

        PaymentResponseDto payment = new PaymentResponseDto(
                1L,
                1L,
                "1232321",
                PaymentStatus.DONE,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                "test.com");

        BookAndBookSaleResponseDto bookSale =
                new BookAndBookSaleResponseDto(
                        1L,
                        "test-img.com",
                        "홍길동전",
                        5,
                        3L,
                        "A 포장",
                        1000,
                        50000);

        SaleDetailResponseDto saleDetailResponseDto =
                new SaleDetailResponseDto(List.of(bookSale), sale, payment);

        given(saleFacade.getSaleDetail(anyLong()))
                .willReturn(saleDetailResponseDto);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/sales/{saleId}", 1L))

                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.bookSaleList[0].bookId").value(bookSale.getBookId()),
                        jsonPath("$.bookSaleList[0].fileUrl").value(bookSale.getFileUrl()),
                        jsonPath("$.bookSaleList[0].bookTitle").value(bookSale.getBookTitle()),
                        jsonPath("$.bookSaleList[0].quantity").value(bookSale.getQuantity()),
                        jsonPath("$.bookSaleList[0].couponId").value(bookSale.getCouponId()),
                        jsonPath("$.bookSaleList[0].packagingType").value(bookSale.getPackagingType()),
                        jsonPath("$.bookSaleList[0].packagingPrice").value(bookSale.getPackagingPrice()),
                        jsonPath("$.bookSaleList[0].paymentAmount").value(bookSale.getPaymentAmount()),
                        jsonPath("$.saleResponseDto.saleId").value(sale.getSaleId()),
                        jsonPath("$.saleResponseDto.memberEmail").value(sale.getMemberEmail()),
                        jsonPath("$.saleResponseDto.saleNumber").value(sale.getSaleNumber()),
                        jsonPath("$.saleResponseDto.saleOrdererName").value(sale.getSaleOrdererName()),
                        jsonPath("$.saleResponseDto.saleOrdererContact").value(sale.getSaleOrdererContact()),
                        jsonPath("$.saleResponseDto.saleReceiverName").value(sale.getSaleReceiverName()),
                        jsonPath("$.saleResponseDto.saleReceiverContact").value(sale.getSaleReceiverContact()),
                        jsonPath("$.saleResponseDto.saleReceiverAddress").value(sale.getSaleReceiverAddress()),
                        jsonPath("$.saleResponseDto.saleDate").isNotEmpty(),
                        jsonPath("$.saleResponseDto.saleShippingDate").isNotEmpty(),
                        jsonPath("$.saleResponseDto.saleDeliveryDate").isNotEmpty(),
                        jsonPath("$.saleResponseDto.saleDeliveryStatus").value(sale.getSaleDeliveryStatus().name()),
                        jsonPath("$.saleResponseDto.saleDeliveryFee").value(sale.getSaleDeliveryFee()),
                        jsonPath("$.saleResponseDto.salePointUsage").value(sale.getSalePointUsage()),
                        jsonPath("$.saleResponseDto.saleTotalPrice").value(sale.getSaleTotalPrice()),
                        jsonPath("$.saleResponseDto.salePaymentStatus").value(sale.getSalePaymentStatus().name()),
                        jsonPath("$.saleResponseDto.saleShippingPostCode").value(sale.getSaleShippingPostCode()),
                        jsonPath("$.paymentResponseDto.paymentId").value(payment.getPaymentId()),
                        jsonPath("$.paymentResponseDto.saleId").value(payment.getSaleId()),
                        jsonPath("$.paymentResponseDto.paymentKey").value(payment.getPaymentKey()),
                        jsonPath("$.paymentResponseDto.paymentStatus").value(payment.getPaymentStatus().name()),
                        jsonPath("$.paymentResponseDto.requestedAt").isNotEmpty(),
                        jsonPath("$.paymentResponseDto.approvedAt").isNotEmpty(),
                        jsonPath("paymentResponseDto.receiptUrl").value(payment.getReceiptUrl()))
                .andDo(document("sale/getSaleDetail_admin/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("saleId").description("주문 ID")
                        ),
                        responseFields(
                                fieldWithPath("bookSaleList[].bookId").description("도서 ID"),
                                fieldWithPath("bookSaleList[].fileUrl").description("도서 이미지 URL"),
                                fieldWithPath("bookSaleList[].bookTitle").description("도서 제목"),
                                fieldWithPath("bookSaleList[].quantity").description("도서 수량"),
                                fieldWithPath("bookSaleList[].couponId").description("적용된 쿠폰 ID"),
                                fieldWithPath("bookSaleList[].packagingType").description("포장 타입"),
                                fieldWithPath("bookSaleList[].packagingPrice").description("포장 가격"),
                                fieldWithPath("bookSaleList[].paymentAmount").description("결제 금액"),
                                fieldWithPath("saleResponseDto.saleId").description("주문 ID"),
                                fieldWithPath("saleResponseDto.memberId").description("회원 ID"),
                                fieldWithPath("saleResponseDto.title").description("주문명"),
                                fieldWithPath("saleResponseDto.memberEmail").description("회원 이메일"),
                                fieldWithPath("saleResponseDto.saleNumber").description("주문 번호"),
                                fieldWithPath("saleResponseDto.saleOrdererName").description("주문자 이름"),
                                fieldWithPath("saleResponseDto.saleOrdererContact").description("주문자 연락처"),
                                fieldWithPath("saleResponseDto.saleReceiverName").description("수령자 이름"),
                                fieldWithPath("saleResponseDto.saleReceiverContact").description("수령자 연락처"),
                                fieldWithPath("saleResponseDto.saleReceiverAddress").description("배송지"),
                                fieldWithPath("saleResponseDto.saleDate").description("주문 일자"),
                                fieldWithPath("saleResponseDto.saleShippingDate").description("출고 일자"),
                                fieldWithPath("saleResponseDto.saleDeliveryDate").description("배송 일자"),
                                fieldWithPath("saleResponseDto.saleDeliveryStatus").description("배송 상태"),
                                fieldWithPath("saleResponseDto.saleDeliveryFee").description("배송비"),
                                fieldWithPath("saleResponseDto.salePointUsage").description("포인트 사용량"),
                                fieldWithPath("saleResponseDto.saleTotalPrice").description("총 주문 금액"),
                                fieldWithPath("saleResponseDto.salePaymentStatus").description("결제 상태"),
                                fieldWithPath("saleResponseDto.saleShippingPostCode").description("배송지 우편번호"),
                                fieldWithPath("paymentResponseDto.paymentId").description("결제 ID"),
                                fieldWithPath("paymentResponseDto.saleId").description("주문 ID"),
                                fieldWithPath("paymentResponseDto.paymentKey").description("결제 키"),
                                fieldWithPath("paymentResponseDto.paymentStatus").description("결제 상태"),
                                fieldWithPath("paymentResponseDto.requestedAt").description("결제 요청 일자"),
                                fieldWithPath("paymentResponseDto.approvedAt").description("결제 승인 일자"),
                                fieldWithPath("paymentResponseDto.receiptUrl").description("영수증 URL")
                        )
                ));


        verify(saleFacade, times(1)).getSaleDetail(anyLong());
    }


    @Test
    @DisplayName("주문 번호로 주문 상세 정보와 주문한 책 정보 조회 - 회원 페이지")
    void testGetSaleWithBooks() throws Exception {

        BookSaleResponseDto bookSale =
                new BookSaleResponseDto(
                        1L,
                        1L,
                        null,
                        "꽃무늬 포장",
                        3000,
                        3,
                        50000);

        SaleWithBookResponseDto responseDto = new SaleWithBookResponseDto(
                "홍길동전",
                1L,
                "ABC1234DEF",
                "test@test.com",
                "Tester",
                "01012341234",
                "Tester",
                "01011112222",
                3000,
                LocalDate.of(2024, 3, 7).plusDays(1),
                LocalDateTime.of(2024, 3, 7, 12, 0),
                "12345",
                "광주광역시 동구 조선대 5길",
                0,
                10000
        );

        responseDto.addBookSale(bookSale);


        given(saleFacade.getSaleWithBookResponseDto(anyString()))
                .willReturn(responseDto);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/sales/{saleNumber}/books", "ABC1234DEF"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.saleId").value(responseDto.getSaleId()),
                        jsonPath("$.saleNumber").value(responseDto.getSaleNumber()),
                        jsonPath("$.memberEmail").value(responseDto.getMemberEmail()),
                        jsonPath("$.saleOrdererName").value(responseDto.getSaleOrdererName()),
                        jsonPath("$.saleOrdererContact").value(responseDto.getSaleOrdererContact()),
                        jsonPath("$.saleReceiverName").value(responseDto.getSaleReceiverName()),
                        jsonPath("$.saleReceiverContact").value(responseDto.getSaleReceiverContact()),
                        jsonPath("$.deliveryFee").value(responseDto.getDeliveryFee()),
                        jsonPath("$.saleDeliveryDate").isNotEmpty(),
                        jsonPath("$.postcode").value(responseDto.getPostcode()),
                        jsonPath("$.address").value(responseDto.getAddress()),
                        jsonPath("$.pointUsage").value(responseDto.getPointUsage()),
                        jsonPath("$.totalPrice").value(responseDto.getTotalPrice())
                ).andDo(document("sale/getSaleBySaleNumber/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("saleNumber").description("주문 번호")
                        )
                ));

        verify(saleFacade, times(1)).getSaleWithBookResponseDto(anyString());
    }

    @Test
    @DisplayName("결제할 주문 정보 조회 테스트")
    void testGetSalePaymentInfo() throws Exception {

        SaleInfoResponseDto responseDto =
                new SaleInfoResponseDto(
                        "홍길동전 외 3권",
                        "ABC1234DEF",
                        "test@test.com",
                        "Tester",
                        "01012341234",
                        45000,
                        LocalDateTime.of(2024, 3, 7, 12, 0)
                );

        given(saleFacade.getSalePaymentInfo(anyString()))
                .willReturn(responseDto);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/sales/{saleNumber}/paymentInfo", "ABC1234DEF"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.saleTitle").value(responseDto.getSaleTitle()),
                        jsonPath("$.saleNumber").value(responseDto.getSaleNumber()),
                        jsonPath("$.memberEmail").value(responseDto.getMemberEmail()),
                        jsonPath("$.saleOrdererName").value(responseDto.getSaleOrdererName()),
                        jsonPath("$.saleOrdererContact").value(responseDto.getSaleOrdererContact()),
                        jsonPath("$.totalPrice").value(responseDto.getTotalPrice()),
                        jsonPath("$.saleDate").isNotEmpty()
                ).andDo(document("sale/getSalePaymentInfoBySaleNumber/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("saleNumber").description("주문 번호")
                        ),
                        responseFields(
                                fieldWithPath("saleTitle").description("주문 제목"),
                                fieldWithPath("saleNumber").description("주문 번호"),
                                fieldWithPath("memberEmail").description("회원 이메일"),
                                fieldWithPath("saleOrdererName").description("주문자 이름"),
                                fieldWithPath("saleOrdererContact").description("주문자 연락처"),
                                fieldWithPath("totalPrice").description("총 주문 금액"),
                                fieldWithPath("saleDate").description("주문 일자")
                        )
                ));

        verify(saleFacade, times(1)).getSalePaymentInfo(anyString());
    }

    @Test
    @DisplayName("주문 상세 정보를 조회 테스트 - 비회원")
    void testGetSaleDetailBySaleNumber() throws Exception {

        BookAndBookSaleResponseDto bookSale =
                new BookAndBookSaleResponseDto(
                        1L,
                        "test-img.com",
                        "홍길동전",
                        5,
                        3L,
                        "A 포장",
                        1000,
                        50000);


        SaleResponseDto sale =
                new SaleResponseDto(
                        1L,
                        1L,
                        "테스트 제목",
                        "test@test.com",
                        "1234",
                        "정승조",
                        "01012345678",
                        "정승조",
                        "01012345678",
                        "광주광역시 동구 조선대 5길",
                        LocalDateTime.of(2024, 3, 7, 12, 0, 0),
                        LocalDateTime.of(2024, 3, 7, 12, 0, 0).plusDays(1),
                        LocalDate.of(2024, 3, 7).plusDays(3),
                        DeliveryStatus.READY,
                        3000,
                        0,
                        10000,
                        SalePaymentStatus.WAITING,
                        "123456"
                );


        PaymentResponseDto payment = new PaymentResponseDto(
                1L,
                1L,
                "1232321",
                PaymentStatus.DONE,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                "test.com");


        SaleDetailResponseDto responseDto =
                new SaleDetailResponseDto(List.of(bookSale), sale, payment);

        given(saleFacade.getGuestSaleDetailBySaleNumber(anyString(), anyString()))
                .willReturn(responseDto);

        mockMvc.perform(get("/api/sales/guest")
                        .param("saleNumber", "12abc23")
                        .param("ordererContact", "01012341234"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.bookSaleList[0].bookId").value(bookSale.getBookId()),
                        jsonPath("$.bookSaleList[0].fileUrl").value(bookSale.getFileUrl()),
                        jsonPath("$.bookSaleList[0].bookTitle").value(bookSale.getBookTitle()),
                        jsonPath("$.bookSaleList[0].quantity").value(bookSale.getQuantity()),
                        jsonPath("$.bookSaleList[0].couponId").value(bookSale.getCouponId()),
                        jsonPath("$.bookSaleList[0].packagingType").value(bookSale.getPackagingType()),
                        jsonPath("$.bookSaleList[0].packagingPrice").value(bookSale.getPackagingPrice()),
                        jsonPath("$.bookSaleList[0].paymentAmount").value(bookSale.getPaymentAmount()),
                        jsonPath("$.saleResponseDto.saleId").value(sale.getSaleId()),
                        jsonPath("$.saleResponseDto.memberEmail").value(sale.getMemberEmail()),
                        jsonPath("$.saleResponseDto.saleNumber").value(sale.getSaleNumber()),
                        jsonPath("$.saleResponseDto.saleOrdererName").value(sale.getSaleOrdererName()),
                        jsonPath("$.saleResponseDto.saleOrdererContact").value(sale.getSaleOrdererContact()),
                        jsonPath("$.saleResponseDto.saleReceiverName").value(sale.getSaleReceiverName()),
                        jsonPath("$.saleResponseDto.saleReceiverContact").value(sale.getSaleReceiverContact()),
                        jsonPath("$.saleResponseDto.saleReceiverAddress").value(sale.getSaleReceiverAddress()),
                        jsonPath("$.saleResponseDto.saleDate").isNotEmpty(),
                        jsonPath("$.saleResponseDto.saleShippingDate").isNotEmpty(),
                        jsonPath("$.saleResponseDto.saleDeliveryDate").isNotEmpty(),
                        jsonPath("$.saleResponseDto.saleDeliveryStatus").value(sale.getSaleDeliveryStatus().name()),
                        jsonPath("$.saleResponseDto.saleDeliveryFee").value(sale.getSaleDeliveryFee()),
                        jsonPath("$.saleResponseDto.salePointUsage").value(sale.getSalePointUsage()),
                        jsonPath("$.saleResponseDto.saleTotalPrice").value(sale.getSaleTotalPrice()),
                        jsonPath("$.saleResponseDto.salePaymentStatus").value(sale.getSalePaymentStatus().name()),
                        jsonPath("$.saleResponseDto.saleShippingPostCode").value(sale.getSaleShippingPostCode()),
                        jsonPath("$.paymentResponseDto.paymentId").value(payment.getPaymentId()),
                        jsonPath("$.paymentResponseDto.saleId").value(payment.getSaleId()),
                        jsonPath("$.paymentResponseDto.paymentKey").value(payment.getPaymentKey()),
                        jsonPath("$.paymentResponseDto.paymentStatus").value(payment.getPaymentStatus().name()),
                        jsonPath("$.paymentResponseDto.requestedAt").isNotEmpty(),
                        jsonPath("$.paymentResponseDto.approvedAt").isNotEmpty(),
                        jsonPath("paymentResponseDto.receiptUrl").value(payment.getReceiptUrl()))
                .andDo(document("sale/getSaleDetail_guest/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("saleNumber").description("주문 번호"),
                                parameterWithName("ordererContact").description("주문자 연락처")
                        ),
                        responseFields(
                                subsectionWithPath("bookSaleList").description("주문한 책 정보"),
                                subsectionWithPath("saleResponseDto").description("주문 정보"),
                                subsectionWithPath("paymentResponseDto").description("결제 정보")
                        )));

        verify(saleFacade, times(1)).getGuestSaleDetailBySaleNumber(anyString(), anyString());
    }

    @Test
    @DisplayName("회원의 ID와 주문 번호를 통해 주문 상세 정보 조회 - 회원 조회")
    void testGetMemberSaleDetailBySaleNumber() throws Exception {

        BookAndBookSaleResponseDto bookSale =
                new BookAndBookSaleResponseDto(
                        1L,
                        "testimg.com",
                        "홍길동전",
                        5,
                        3L,
                        "A 포장",
                        1000,
                        50000);

        SaleResponseDto responseDto =
                new SaleResponseDto(
                        1L,
                        1L,
                        "테스트 제목",
                        "test@test.com",
                        "1234",
                        "정승조",
                        "01012345678",
                        "정승조",
                        "01012345678",
                        "광주광역시 동구 조선대 5길",
                        LocalDateTime.of(2024, 3, 7, 12, 0, 0),
                        LocalDateTime.of(2024, 3, 7, 12, 0, 0).plusDays(1),
                        LocalDate.of(2024, 3, 7).plusDays(3),
                        DeliveryStatus.READY,
                        3000,
                        0,
                        10000,
                        SalePaymentStatus.WAITING,
                        "123456"
                );

        PaymentResponseDto payment = new PaymentResponseDto(
                1L,
                1L,
                "1232321",
                PaymentStatus.DONE,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                "test.com");


        SaleDetailResponseDto sale = new SaleDetailResponseDto(List.of(bookSale), responseDto, payment);

        given(saleFacade.getMemberSaleDetailBySaleNumber(anyString(), anyLong()))
                .willReturn(sale);

        mockMvc.perform(get("/api/sales/member")
                        .param("saleNumber", "1234")
                        .param("memberId", "1"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.bookSaleList[0].bookId").value(bookSale.getBookId()),
                        jsonPath("$.bookSaleList[0].fileUrl").value(bookSale.getFileUrl()),
                        jsonPath("$.bookSaleList[0].bookTitle").value(bookSale.getBookTitle()),
                        jsonPath("$.bookSaleList[0].quantity").value(bookSale.getQuantity()),
                        jsonPath("$.bookSaleList[0].couponId").value(bookSale.getCouponId()),
                        jsonPath("$.bookSaleList[0].packagingType").value(bookSale.getPackagingType()),
                        jsonPath("$.bookSaleList[0].packagingPrice").value(bookSale.getPackagingPrice()),
                        jsonPath("$.bookSaleList[0].paymentAmount").value(bookSale.getPaymentAmount()),
                        jsonPath("$.saleResponseDto.saleId").value(responseDto.getSaleId()),
                        jsonPath("$.saleResponseDto.title").value(responseDto.getTitle()),
                        jsonPath("$.saleResponseDto.memberEmail").value(responseDto.getMemberEmail()),
                        jsonPath("$.saleResponseDto.saleNumber").value(responseDto.getSaleNumber()),
                        jsonPath("$.saleResponseDto.saleOrdererName").value(responseDto.getSaleOrdererName()),
                        jsonPath("$.saleResponseDto.saleOrdererContact").value(responseDto.getSaleOrdererContact()),
                        jsonPath("$.saleResponseDto.saleReceiverName").value(responseDto.getSaleReceiverName()),
                        jsonPath("$.saleResponseDto.saleReceiverContact").value(responseDto.getSaleReceiverContact()),
                        jsonPath("$.saleResponseDto.saleReceiverAddress").value(responseDto.getSaleReceiverAddress()),
                        jsonPath("$.saleResponseDto.saleDate").isNotEmpty(),
                        jsonPath("$.saleResponseDto.saleShippingDate").isNotEmpty(),
                        jsonPath("$.saleResponseDto.saleDeliveryDate").isNotEmpty(),
                        jsonPath("$.saleResponseDto.saleDeliveryStatus").value(
                                responseDto.getSaleDeliveryStatus().name()),
                        jsonPath("$.saleResponseDto.saleDeliveryFee").value(responseDto.getSaleDeliveryFee()),
                        jsonPath("$.saleResponseDto.salePointUsage").value(responseDto.getSalePointUsage()),
                        jsonPath("$.saleResponseDto.saleTotalPrice").value(responseDto.getSaleTotalPrice()),
                        jsonPath("$.saleResponseDto.salePaymentStatus").value(
                                responseDto.getSalePaymentStatus().name()),
                        jsonPath("$.saleResponseDto.saleShippingPostCode").value(responseDto.getSaleShippingPostCode()),
                        jsonPath("$.paymentResponseDto.paymentId").value(payment.getPaymentId()),
                        jsonPath("$.paymentResponseDto.saleId").value(payment.getSaleId()),
                        jsonPath("$.paymentResponseDto.paymentKey").value(payment.getPaymentKey()),
                        jsonPath("$.paymentResponseDto.paymentStatus").value(payment.getPaymentStatus().name()),
                        jsonPath("$.paymentResponseDto.requestedAt").isNotEmpty(),
                        jsonPath("$.paymentResponseDto.approvedAt").isNotEmpty(),
                        jsonPath("paymentResponseDto.receiptUrl").value(payment.getReceiptUrl())
                )
                .andDo(document("sale/getSaleDetail_member/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("saleNumber").description("주문 번호"),
                                parameterWithName("memberId").description("회원 ID")
                        ),
                        responseFields(
                                subsectionWithPath("bookSaleList").description("주문한 책 정보"),
                                subsectionWithPath("saleResponseDto").description("주문 정보"),
                                subsectionWithPath("paymentResponseDto").description("결제 정보")
                        )
                ));

        verify(saleFacade, times(1)).getMemberSaleDetailBySaleNumber(anyString(), anyLong());
    }

    @Test
    @DisplayName("회원 ID로 회원의 모든 주문 리스트 조회")
    void testGetSalesByMemberId() throws Exception {
        SaleInfoResponseDto saleInfo
                = new SaleInfoResponseDto("홍길동전 외 3권",
                "123aqbc4",
                "test@test.com",
                "Tester",
                "010123211234",
                45000,
                LocalDateTime.of(2024, 3, 7, 12, 0));

        PageInfo pageInfo = PageInfo.builder()
                .page(1)
                .size(10)
                .build();

        PagedResponse<List<SaleInfoResponseDto>> pagedResponse = new PagedResponse<>(List.of(saleInfo), pageInfo);

        given(saleFacade.getSalesByMemberId(anyLong(), any()))
                .willReturn(pagedResponse);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/sales/member/{memberId}", 1L))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.data[0].saleTitle").value(saleInfo.getSaleTitle()),
                        jsonPath("$.data[0].saleNumber").value(saleInfo.getSaleNumber()),
                        jsonPath("$.data[0].memberEmail").value(saleInfo.getMemberEmail()),
                        jsonPath("$.data[0].saleOrdererName").value(saleInfo.getSaleOrdererName()),
                        jsonPath("$.data[0].saleOrdererContact").value(saleInfo.getSaleOrdererContact()),
                        jsonPath("$.data[0].totalPrice").value(saleInfo.getTotalPrice()),
                        jsonPath("$.data[0].saleDate").isNotEmpty(),
                        jsonPath("$.pageInfo.page").value(pageInfo.getPage()),
                        jsonPath("$.pageInfo.size").value(pageInfo.getSize()))
                .andDo(document("sale/getSaleListByMemberId/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberId").description("회원 ID")
                        ),
                        responseFields(
                                fieldWithPath("data[].saleTitle").description("주문 제목"),
                                fieldWithPath("data[].saleNumber").description("주문 번호"),
                                fieldWithPath("data[].memberEmail").description("회원 이메일"),
                                fieldWithPath("data[].saleOrdererName").description("주문자 이름"),
                                fieldWithPath("data[].saleOrdererContact").description("주문자 연락처"),
                                fieldWithPath("data[].totalPrice").description("총 주문 금액"),
                                fieldWithPath("data[].saleDate").description("주문 일자"),
                                fieldWithPath("pageInfo.page").description("페이지 번호"),
                                fieldWithPath("pageInfo.size").description("페이지 크기"),
                                fieldWithPath("pageInfo.totalElements").description("총 주문 수"),
                                fieldWithPath("pageInfo.totalPages").description("총 페이지 수")
                        )
                ));

        verify(saleFacade, times(1)).getSalesByMemberId(anyLong(), any());
    }

    @Test
    @DisplayName("주문 배송 상태 업데이트 테스트")
    void testUpdateDeliveryStatus() throws Exception {
        SaleDeliveryUpdateRequestDto deliveryStatus = new SaleDeliveryUpdateRequestDto();
        ReflectionTestUtils.setField(deliveryStatus, "deliveryStatus", DeliveryStatus.IN_PROGRESS);

        String json = objectMapper.writeValueAsString(deliveryStatus);

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/sales/{saleId}/delivery/status", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(document("sale/updateSaleDeliveryStatus/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("saleId").description("주문 ID")
                        ),
                        requestFields(
                                fieldWithPath("deliveryStatus").description("배송 상태").type(Enum.class)
                        ))
                );

        verify(saleFacade, times(1)).updateSaleDeliveryStatus(anyLong(), any());
    }

    @Test
    @DisplayName("주문 상태를 취소로 변경하는 테스트")
    void testCancelSale() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/sales/{saleId}/cancel", 1L))
                .andExpect(status().isOk())
                .andDo(document("sale/updateSaleStatus/cancel",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("saleId").description("주문 ID")
                        )
                ));

        verify(saleFacade, times(1)).cancelSale(anyLong());
    }

    @Test
    @DisplayName("회원 ID와 도서 ID를 통해 주문이 진행되었는지 확인하는 테스트")
    void testCheckSaleByMemberIdAndBookId() throws Exception {

        SaleCheckResponseDto responseDto = new SaleCheckResponseDto(true);

        given(saleFacade.checkSaleByMemberIdAndBookId(anyLong(), anyLong()))
                .willReturn(responseDto);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/sales/check/{memberId}/{bookId}", 1L, 1L))
                .andExpect(status().isOk())
                .andDo(document("sale/checkSaleByMemberIdAndBookId/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberId").description("회원 ID"),
                                parameterWithName("bookId").description("도서 ID")
                        ),
                        responseFields(
                                fieldWithPath("isExist").description("주문 여부")
                        )
                ));

        verify(saleFacade, times(1)).checkSaleByMemberIdAndBookId(anyLong(), anyLong());
    }
}