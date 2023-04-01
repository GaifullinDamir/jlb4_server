
class Main
{

    public static void main(String [] args) {

        Services.Calculator calculator = new Services.Calculator();
        try{
            var expression = "9+6*5-7.5/8";
            Services.Checker.checkExpression(expression);
            var result = calculator.calculate(expression);
            System.out.println(result);
        }catch(Exception e){
            System.out.println(e);
        }
    }

}
