package store.ckin.api.sale.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import store.ckin.api.booksale.dto.response.BookAndBookSaleResponseDto;
import store.ckin.api.common.domain.PageInfo;
import store.ckin.api.common.dto.PagedResponse;
import store.ckin.api.payment.dto.response.PaymentResponseDto;
import store.ckin.api.sale.dto.request.SaleCreateRequestDto;
import store.ckin.api.sale.dto.response.SaleDetailResponseDto;
import store.ckin.api.sale.dto.response.SaleInfoResponseDto;
import store.ckin.api.sale.dto.response.SaleResponseDto;
import store.ckin.api.sale.dto.response.SaleWithBookResponseDto;
import store.ckin.api.sale.entity.Sale;
import store.ckin.api.sale.facade.SaleFacade;

/**
 * 주문 컨트롤러 테스트.
 *
 * @author 정승조
 * @version 2024. 03. 07.
 */

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

        SaleCreateRequestDto requestDto = new SaleCreateRequestDto();
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
                .andDo(print());

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
                        Sale.DeliveryStatus.READY,
                        3000,
                        0,
                        10000,
                        Sale.PaymentStatus.WAITING,
                        "123456"
                );

        PageInfo pageInfo = new PageInfo(1, 10, 1, 1);
        PagedResponse<List<SaleResponseDto>> pagedResponse =
                new PagedResponse<>(List.of(responseDto), pageInfo);

        given(saleFacade.getSales(any()))
                .willReturn(pagedResponse);

        mockMvc.perform(get("/api/sales"))
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
                        jsonPath("$.pageInfo.totalPages").value(pageInfo.getTotalPages())
                ).andDo(print());

        verify(saleFacade, times(1)).getSales(any());
    }

    @Test
    @DisplayName("주문 상세 조회 테스트")
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
                        Sale.DeliveryStatus.READY,
                        3000,
                        0,
                        10000,
                        Sale.PaymentStatus.WAITING,
                        "123456"
                );

        PaymentResponseDto payment = new PaymentResponseDto(
                1L,
                1L,
                "1232321",
                "1234",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                "test.com");

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

        SaleDetailResponseDto saleDetailResponseDto =
                new SaleDetailResponseDto(List.of(bookSale), sale, payment);

        given(saleFacade.getSaleDetail(anyLong()))
                .willReturn(saleDetailResponseDto);

        mockMvc.perform(get("/api/sales/{saleId}", 1L))

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
                        jsonPath("$.paymentResponseDto.paymentStatus").value(payment.getPaymentStatus()),
                        jsonPath("$.paymentResponseDto.requestedAt").isNotEmpty(),
                        jsonPath("$.paymentResponseDto.approvedAt").isNotEmpty(),
                        jsonPath("paymentResponseDto.receiptUrl").value(payment.getReceiptUrl())
                );


        verify(saleFacade, times(1)).getSaleDetail(anyLong());
    }

    @Test
    @DisplayName("주문 결제 상태를 완료(PAID)로 변경 테스트")
    void testUpdateSalePaymentPaidStatus() throws Exception {

        mockMvc.perform(put("/api/sales/{saleId}", 1L))
                .andExpect(status().isOk())
                .andDo(print());

        verify(saleFacade, times(1)).updateSalePaymentPaidStatus(1L);
    }

    @Test
    @DisplayName("주문 ID로 주문 상세 정보와 주문한 책 정보 조회 테스트")
    void testGetSaleWithBooks() throws Exception {

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


        given(saleFacade.getSaleWithBookResponseDto(anyString()))
                .willReturn(responseDto);
        mockMvc.perform(get("/api/sales/{saleNumber}/books", "ABC1234DEF"))
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
                ).andDo(print());

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

        mockMvc.perform(get("/api/sales/{saleNumber}/paymentInfo", "ABC1234DEF"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.saleTitle").value(responseDto.getSaleTitle()),
                        jsonPath("$.saleNumber").value(responseDto.getSaleNumber()),
                        jsonPath("$.memberEmail").value(responseDto.getMemberEmail()),
                        jsonPath("$.saleOrdererName").value(responseDto.getSaleOrdererName()),
                        jsonPath("$.saleOrdererContact").value(responseDto.getSaleOrdererContact()),
                        jsonPath("$.totalPrice").value(responseDto.getTotalPrice())
                ).andDo(print());

        verify(saleFacade, times(1)).getSalePaymentInfo(anyString());
    }

    @Test
    @DisplayName("비회원 주문 상세 정보를 조회 테스트")
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
                        Sale.DeliveryStatus.READY,
                        3000,
                        0,
                        10000,
                        Sale.PaymentStatus.WAITING,
                        "123456"
                );


        PaymentResponseDto payment = new PaymentResponseDto(
                1L,
                1L,
                "1232321",
                "1234",
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
                        jsonPath("$.paymentResponseDto.paymentStatus").value(payment.getPaymentStatus()),
                        jsonPath("$.paymentResponseDto.requestedAt").isNotEmpty(),
                        jsonPath("$.paymentResponseDto.approvedAt").isNotEmpty(),
                        jsonPath("paymentResponseDto.receiptUrl").value(payment.getReceiptUrl())
                );
    }

    @Test
    @DisplayName("회원의 ID와 주분 번호를 통해 주문 상세 정보 조회")
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
                        Sale.DeliveryStatus.READY,
                        3000,
                        0,
                        10000,
                        Sale.PaymentStatus.WAITING,
                        "123456"
                );

        PaymentResponseDto payment = new PaymentResponseDto(
                1L,
                1L,
                "1232321",
                "1234",
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
                        jsonPath("$.paymentResponseDto.paymentStatus").value(payment.getPaymentStatus()),
                        jsonPath("$.paymentResponseDto.requestedAt").isNotEmpty(),
                        jsonPath("$.paymentResponseDto.approvedAt").isNotEmpty(),
                        jsonPath("paymentResponseDto.receiptUrl").value(payment.getReceiptUrl())
                )
                .andDo(print());
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

        mockMvc.perform(get("/api/sales/member/{memberId}", 1L))
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
                        jsonPath("$.pageInfo.size").value(pageInfo.getSize())
                );
    }
}