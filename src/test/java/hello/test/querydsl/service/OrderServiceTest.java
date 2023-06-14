package hello.test.querydsl.service;

import hello.test.querydsl.domain.Address;
import hello.test.querydsl.domain.Member;
import hello.test.querydsl.domain.Order;
import hello.test.querydsl.domain.OrderStatus;
import hello.test.querydsl.domain.exception.NotEnoughStockException;
import hello.test.querydsl.domain.item.Book;
import hello.test.querydsl.domain.item.Item;
import hello.test.querydsl.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class OrderServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        // given

        Member member = createMember();
        Item item = createBook("시골 JPA", 10000, 10);
        int orderCount = 2;

        // when
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        // then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, getOrder.getStatus(),"상품 주문시 상태는 ORDER");
        assertEquals(1, getOrder.getOrderItems().size(), "주문한 상품 종류 수가 정확해야 한다.");
        assertEquals(10000 * 2, getOrder.getTotalPrice(), "주문 가격은 가격 * 수량이다.");
        assertEquals(8, item.getStockQuantity(), "주문 수량만큼 재고가 줄어야 한다.");

    }

    @Test
    public void 상품주문_재고수량초과() throws Exception {
        // given

        Member member = createMember();
        Item item = createBook("시골 JPA", 10000, 10);

        // when
        int orderCount = 11;

        // then

        assertThrows(NotEnoughStockException.class,() -> orderService.order(member.getId(), item.getId(), orderCount));

    }

    @Test
    public void 주문취소() throws Exception {
        // given

        Member member = createMember();
        Item item = createBook("시골 JPA", 10000, 10);
        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        // when

        orderService.cancelOrder(orderId);

        // then

        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.CANCEL, getOrder.getStatus(), "주문 취소시 상태는 CANCEL이다.");
        assertEquals(item.getStockQuantity(), 10, "주문이 취소된 상품은 그만큼 재고가 증가해야 한다.");
    }

    private Item createBook(String name, int price, int stockQuantity) {

        Book book = new Book();
        book.setName(name);
        book.setStockQuantity(stockQuantity);
        book.setPrice(price);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }
}