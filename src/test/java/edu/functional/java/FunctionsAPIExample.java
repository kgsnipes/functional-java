package edu.functional.java;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.function.*;

public class FunctionsAPIExample {

    /*
        lambda expression
        Function
        BiFunction
        Unary operator
        Binary Operator
        Predicate
        BiPredicate
        Supplier
        Consumer

     */

    @Test
    public void simpleLambda()
    {
        Function<Integer,Integer> square=(x)->x*x;
        System.out.println(square.apply(4));
    }

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
       BiFunction<Integer,String,String> concatNumberWithString=(a,b)->a+b;
       System.out.println(concatNumberWithString.apply(100,"-PRD"));
       Assertions.assertEquals("100-PRD",concatNumberWithString.apply(100,"-PRD"));
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
    public void binaryOperatorTest()
    {
        BinaryOperator<Integer> sum=(a,b)->a+b;

        System.out.println(sum.apply(10,20));
        Assertions.assertEquals(30,sum.apply(10,20).intValue());
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
    public void supplierConsumerTest()
    {
        Supplier<Double> randomNumber=()->new Random().nextDouble();
        Consumer<Double> consumer=System.out::println;
        consumer.accept(randomNumber.get());

    }

    @Test
    public void cartFlowTest()
    {
        Function<String,Cart> createCart=(cartId)->{
           Cart cart =new Cart();
           cart.setCartId(cartId);
           return cart;
        };

        Function<String,Double> getPrice=(product)->{
           switch (product)
           {
               case "P1001":
                   return 100.00;

           }
           return 0.00;
        };

        BiFunction<AddToCartDTO,Cart,Cart> addToCart=(product,cart)->{
            LineItem lineItem=new LineItem(product.getProduct(),product.getQty(),getPrice.apply(product.getProduct()));
            cart.getLineItems().add(lineItem);
            return cart;
        };

        UnaryOperator<Cart> calculateCart=(cart)->{
            cart.setCartTotal(cart.getLineItems().stream().map(lineItem -> lineItem.getUnitPrice()).reduce((price,total)->total=total+price).orElse(0.0));
            return cart;
        };

        Consumer<Cart> displayCart=System.out::println;

        Cart cart=createCart.apply("1001");

        addToCart.andThen(calculateCart).apply(new AddToCartDTO("P1001",1),cart);

        displayCart.accept(cart);
    }



    @Test
    public void lambdaThreadExample()
    {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("hello world");
//            }
//        }).start();

        new Thread(()->{
            System.out.println("hello lambda expression");
        }).start();

        Executors.newCachedThreadPool().submit(()->{
            System.out.println("hello executor task");
        });
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

    @Override
    public String toString() {
        try
        {
            return new ObjectMapper().writeValueAsString(this);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }
}

class AddToCartDTO
{
    String product;
    int qty;

    public AddToCartDTO(String product, int qty) {
        this.product = product;
        this.qty = qty;
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
