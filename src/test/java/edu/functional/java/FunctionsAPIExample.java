package edu.functional.java;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.*;

public class FunctionsAPIExample {

   @Test
   public void incrementNumberTest()
   {
       Function<Long,Long> incrementNumber=(num)->++num;
       System.out.println(incrementNumber.apply(10l));
       Assertions.assertEquals(11l,incrementNumber.apply(10l).longValue());
   }

   @Test
   public void biFunctionTest()
   {
       BiFunction<Integer,Integer,Integer> addTwoNumbers=(a,b)->a+b;
       System.out.println(addTwoNumbers.apply(1,2));
       Assertions.assertEquals(3,addTwoNumbers.apply(1,2).intValue());
   }

   @Test
   public void predicateTest()
   {
       Predicate<String> isNull=(obj)-> Objects.isNull(obj);
       Predicate<String> isEmpty=(obj)-> StringUtils.isEmpty(obj);
       System.out.println(isNull.test("hello"));
       System.out.println(isNull.and(isEmpty).test("hello"));
       Assertions.assertEquals(false,isNull.test("hello"));
       Assertions.assertEquals(false,isNull.and(isEmpty).test("hello")); 
   }

   @Test
   public void biPredicateTest()
   {
       BiPredicate<Integer,Integer> isGreater=(x,y)->x>y;
       System.out.println(isGreater.test(10,20));
       Assertions.assertEquals(false,isGreater.test(10,20));
   }

    Integer functionAsArgument(Function<Integer,Integer> fn,Integer input)
    {
        return (Integer) fn.apply(input);
    }

    @Test
    public void executeFunctionAsArgumentTest()
    {
        System.out.println(functionAsArgument((a)->++a,1).intValue());
        Assertions.assertEquals(2,functionAsArgument((a)->++a,1).intValue());
    }

    @Test
    public void unaryOperatorTest()
    {
        UnaryOperator<Point> incrementX=(point)->{
            point.setX(point.getX()+10);
            return point;
        };

        System.out.println(incrementX.apply(new Point(10,20)));
        Assertions.assertEquals(20,incrementX.apply(new Point(10,20)).getX());
    }


    @Test
    public void chainFunctions()
    {
        Function<String,Integer> strlen=(str)->str.length();
        Function<Integer,Integer> square=(x)->x*x;
        Integer result=strlen.andThen(square).apply("hello world");
        System.out.println(result);
    }

    @Test
    public void cartFlowTest()
    {
        Function<String,Cart> createCart=(cartId)->{
           Cart cart =new Cart();
           cart.setCartId(cartId);
           return cart;
        };

        BiFunction<String,Cart,Cart> addToCart=(product,cart)->{
            LineItem lineItem=new LineItem(product,1,100.0);
            cart.getLineItems().add(lineItem);
            return cart;
        };

        UnaryOperator<Cart> calculateCart=(cart)->{
            cart.setCartTotal(cart.getLineItems().stream().map(lineItem -> lineItem.getUnitPrice()).reduce((price,total)->total=total+price).orElse(0.0));
            return cart;
        };

        Cart cart=createCart.apply("1001");
        addToCart.andThen(calculateCart).apply("P1001",cart);
        System.out.println(cart.getCartTotal());
    }



    @Test
    public void lambdaExample()
    {
        new Thread(()->{
            System.out.println("hello lambda expression");
        }).start();
    }
}


class Point
{
    int x;
    int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Point  x:"+this.x+" y:"+this.y;
    }
}


class Cart
{
    String cartId;
    double cartTotal;
    List<LineItem> lineItems=new ArrayList<>();

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public double getCartTotal() {
        return cartTotal;
    }

    public void setCartTotal(double cartTotal) {
        this.cartTotal = cartTotal;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }
}

class LineItem
{
    String product;
    int qty;
    double unitPrice;

    public LineItem(String product, int qty, double unitPrice) {
        this.product = product;
        this.qty = qty;
        this.unitPrice = unitPrice;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
}
