function mailSender() {
    let email=document.getElementById("email").value;
    axios.get("/api/mail/send",{params: {email:email}})
        .then(function (response) {
            let data=JSON.parse(JSON.stringify(response.data));
            alert(data.message)
            console.log(response.data)
        })
}


function checkSize() {
    var uploadField = document.getElementById("file");

    uploadField.onchange = function() {
        if(this.files[0].size > 2097152){
            alert("File is too big!");
            this.value = "";
        }
    }
}


function saveFile() {

}
