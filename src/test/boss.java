package test;

public class boss {
  private office office;
  private car car;
  @Autowired
  public office getOffice() {
	return office;
}

public void setOffice(office office) {
	this.office = office;
}

public car getCar() {
	return car;
}

public void setCar(car car) {
	this.car = car;
}
  public boss(car car ,office office){
      this.car = car;
      this.office = office ;
  }
  public boss(){
	  
  }
  

public String tostring(){
	  return "this boss has"+car.tostring()+"and in"+office.tostring();
	 
  }
  
  
  
  
  
  
  


}
