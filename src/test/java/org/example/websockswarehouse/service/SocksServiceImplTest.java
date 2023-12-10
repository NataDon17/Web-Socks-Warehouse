package org.example.websockswarehouse.service;

import org.example.websockswarehouse.dto.SockDTO;
import org.example.websockswarehouse.exeption.SockInvalidParameterException;
import org.example.websockswarehouse.exeption.SockNotFoundException;
import org.example.websockswarehouse.exeption.validation.ValidationRequestParam;
import org.example.websockswarehouse.model.SocksOperation;
import org.example.websockswarehouse.repository.SocksRepository;
import org.example.websockswarehouse.service.impl.SocksServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.example.websockswarehouse.constant.SocksConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SocksServiceImplTest {
    @Mock
    private SocksRepository repositoryMock;
    @InjectMocks
    private SocksServiceImpl out;

    @ParameterizedTest
    @MethodSource("provideParamsForCekParamTests")
    public void checkParamsTest(SockDTO sockDTO) {
        assertThrows(SockInvalidParameterException.class, () -> ValidationRequestParam.checkParams(sockDTO));
    }

    @Test
    public void shouldCallRepositoryMethodWhenGetQuantitySocks() {
        when(repositoryMock.findAll())
                .thenReturn(List.of(SOCK_3.toSock()));
        assertEquals(10, out.getQuantitySocks(SOCK_3));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForTests")
    public void shouldCallRepositoryMethodWhenArrivalElementAbsent(SockDTO sockDTO) {
        when(repositoryMock.save(sockDTO.toSock()))
                .thenReturn(sockDTO.toSock());
        out.arrivalSocks(sockDTO);
        verify(repositoryMock, times(1)).save(sockDTO.toSock());
    }

    @Test
    public void shouldCallRepositoryMethodWhenArrivalElementIsPresent() {
        when(repositoryMock.findAll())
                .thenReturn(List.of(SOCK_1.toSock()));
        assertEquals(SOCK_1.getQuantity(), out.getQuantitySocks(SOCK_1));
        out.arrivalSocks(SOCK_1);
        verify(repositoryMock, times(1)).updateQuantityWhenSocksArrive(
                SOCK_1.getColor(),
                SOCK_1.getCottonPart(),
                SOCK_1.getQuantity()
        );
    }

    @Test
    public void shouldCallRepositoryMethodWhenDispatchElementIsPresent() {
        when(repositoryMock.findAll())
                .thenReturn(List.of(SOCK_4.toSock()));
        assertEquals(SOCK_4.getQuantity(), out.getQuantitySocks(SOCK_4));
        out.dispatchSocks(SOCK_4);
        verify(repositoryMock, times(1)).updateQuantityWhenSocksDisposal(
                SOCK_4.getColor(),
                SOCK_4.getCottonPart(),
                SOCK_4.getQuantity()
        );
    }

    @Test
    public void shouldCallRepositoryMethodWhenDispatchElementNotFound() {
        when(repositoryMock.findAll())
                .thenReturn(List.of(SOCK_4.toSock()));
        assertThrows(SockNotFoundException.class, () -> out.dispatchSocks(SOCK_2));
    }

    @Test
    public void shouldCallRepositoryMethodWhenFindElement() {
        when(repositoryMock.findAll())
                .thenReturn(List.of(SOCK_1.toSock(), SOCK_2.toSock(), SOCK_3.toSock(), SOCK_4.toSock()));
        int result1 = out.findSocks("gray", 30, SocksOperation.equal);
        int result2 = out.findSocks("red", 50, SocksOperation.moreThan);
        int result3 = out.findSocks("red", 70, SocksOperation.lessThan);
        int result4 = out.findSocks("gray", 30, SocksOperation.moreThan);
        assertEquals(13, result1);
        assertEquals(5, result2);
        assertEquals(2, result3);
        assertEquals(0, result4);
    }

    public static Stream<Arguments> provideParamsForTests() {
        return Stream.of(
                Arguments.of(SOCK_1),
                Arguments.of(SOCK_2),
                Arguments.of(SOCK_3)
        );
    }

    public static Stream<Arguments> provideParamsForCekParamTests() {
        SockDTO sockWithNegativeQuantity = new SockDTO("red", 50, -1);
        SockDTO sockWithNegativeCottonPart = new SockDTO("red", -50, 1);
        SockDTO sockWithInvalidCottonPart = new SockDTO("red", 200, 1);
        SockDTO sockWithEmptyColor = new SockDTO("", 50, 1);
        return Stream.of(
                Arguments.of(sockWithNegativeQuantity),
                Arguments.of(sockWithNegativeCottonPart),
                Arguments.of(sockWithInvalidCottonPart),
                Arguments.of(sockWithEmptyColor)
        );
    }
}
