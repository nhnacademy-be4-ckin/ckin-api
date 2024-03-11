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
import org.springframework.test.web.servlet.MockMvc;
import store.ckin.api.common.domain.PageInfo;
import store.ckin.api.common.dto.PagedResponse;
import store.ckin.api.sale.dto.request.SaleCreateRequestDto;
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
                .willReturn(1L);

        SaleCreateRequestDto requestDto = new SaleCreateRequestDto(
                1L,
                "정승조",
                "01012345678",
                "정승조",
                "01012345678",
                3000,
                LocalDate.of(2024, 3, 7),
                "123456",
                "광주광역시 동구 조선대 5길 ",
                "IT 융합대학",
                0,
                10000
        );

        String json = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().json("1"))
                .andDo(print());

        verify(saleFacade, times(1)).createSale(any());
    }

    @Test
    @DisplayName("주문 목록 조회 테스트")
    void testGetSales() throws Exception {
        SaleResponseDto responseDto =
                new SaleResponseDto(
                        1L,
                        "test@test.com",
                        "1234",
                        "정승조",
                        "01012345678",
                        "정승조",
                        "01012345678",
                        "광주광역시 동구 조선대 5길",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(1),
                        LocalDate.now().plusDays(3),
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
                        jsonPath("$.data[0].saleShippingDate").value(responseDto.getSaleShippingDate().toString()),
                        jsonPath("$.data[0].saleDeliveryDate").value(responseDto.getSaleDeliveryDate().toString()),
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
        SaleResponseDto responseDto =
                new SaleResponseDto(
                        1L,
                        "test@test.com",
                        "1234",
                        "정승조",
                        "01012345678",
                        "정승조",
                        "01012345678",
                        "광주광역시 동구 조선대 5길",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(1),
                        LocalDate.now().plusDays(3),
                        Sale.DeliveryStatus.READY,
                        3000,
                        0,
                        10000,
                        Sale.PaymentStatus.WAITING,
                        "123456"
                );

        given(saleFacade.getSaleDetail(anyLong()))
                .willReturn(responseDto);

        mockMvc.perform(get("/api/sales/{saleId}", 1L))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.saleId").value(responseDto.getSaleId()),
                        jsonPath("$.memberEmail").value(responseDto.getMemberEmail()),
                        jsonPath("$.saleNumber").value(responseDto.getSaleNumber()),
                        jsonPath("$.saleOrdererName").value(responseDto.getSaleOrdererName()),
                        jsonPath("$.saleOrdererContact").value(responseDto.getSaleOrdererContact()),
                        jsonPath("$.saleReceiverName").value(responseDto.getSaleReceiverName()),
                        jsonPath("$.saleReceiverContact").value(responseDto.getSaleReceiverContact()),
                        jsonPath("$.saleReceiverAddress").value(responseDto.getSaleReceiverAddress()),
                        jsonPath("$.saleDeliveryDate").value(responseDto.getSaleDeliveryDate().toString()),
                        jsonPath("$.saleDeliveryStatus").value(responseDto.getSaleDeliveryStatus().name()),
                        jsonPath("$.saleDeliveryFee").value(responseDto.getSaleDeliveryFee()),
                        jsonPath("$.salePointUsage").value(responseDto.getSalePointUsage()),
                        jsonPath("$.saleTotalPrice").value(responseDto.getSaleTotalPrice()),
                        jsonPath("$.salePaymentStatus").value(responseDto.getSalePaymentStatus().name()),
                        jsonPath("$.saleShippingPostCode").value(responseDto.getSaleShippingPostCode())
                ).andDo(print());

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
                1L,
                "ABC1234DEF",
                "test@test.com",
                "Tester",
                "01012341234",
                "Tester",
                "01011112222",
                3000,
                LocalDate.now().plusDays(1),
                "12345",
                "광주광역시 동구 조선대 5길",
                0,
                10000
        );


        given(saleFacade.getSaleWithBookResponseDto(anyLong()))
                .willReturn(responseDto);
        mockMvc.perform(get("/api/sales/{saleId}/books", 1L))

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
                        jsonPath("$.saleDeliveryDate").value(responseDto.getSaleDeliveryDate().toString()),
                        jsonPath("$.postcode").value(responseDto.getPostcode()),
                        jsonPath("$.address").value(responseDto.getAddress()),
                        jsonPath("$.pointUsage").value(responseDto.getPointUsage()),
                        jsonPath("$.totalPrice").value(responseDto.getTotalPrice())
                ).andDo(print());

        verify(saleFacade, times(1)).getSaleWithBookResponseDto(anyLong());
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
                        45000
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
}