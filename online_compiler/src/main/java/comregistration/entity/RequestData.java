package comregistration.entity;

public class RequestData {

		private FormData formData;
		private FormArray formArray;
		
		public FormData getFormData() {
			return formData;
		}
		public void setFormData(FormData formData) {
			this.formData = formData;
		}
		public FormArray getFormArray() {
			return formArray;
		}
		public void setFormArray(FormArray formAray) {
			this.formArray = formAray;
		}
		@Override
		public String toString() {
			return "RequestData [formData=" + formData + ", formArray=" + formArray + "]";
		}
		public RequestData(FormData formData, FormArray formArray) {
			super();
			this.formData = formData;
			this.formArray = formArray;
		}
		public RequestData() {
			super();
			// TODO Auto-generated constructor stub
		}
		
		
		
		
}
