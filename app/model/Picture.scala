package model
import siena._
import com.google.appengine.api.datastore.Blob

@Table("picture")
class Picture extends Model {
	@Id 
	var id : Long = _
	@NotNull
	@Column(Array("ImageContent"))
	var ImageContent : Blob = new Blob(Array[Byte]())
   
   def this(blob : Array[Byte]) ={
		this()
		if (blob.length > 1048576){
			throw new IllegalArgumentException("Image is bigger than 1MB (1048576 Bytes)")
		}
		this.ImageContent = new Blob(blob) 
		insert()
	}
}

object Picture extends Model {
	def findById(id:Long):Picture = {
	  return Model.all(classOf[Picture]).filter("id", id).get()
	}
}