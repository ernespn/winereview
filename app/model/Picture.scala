package model
import siena._

@Table("picture")
class Picture extends Model {
	@Id 
	var id : Long = _
   @NotNull var filename : String = _
   @NotNull var filepath : String = _
   @NotNull var filesize : Long = _
}