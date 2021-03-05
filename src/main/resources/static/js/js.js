function mailSender() {
    let email=document.getElementById("email").value;
    // document.getElementById("button").style.visibility = 'hidden';

    if (validateEmail(email)){
        document.getElementById("root").innerHTML="<button class=\"buttonload btn btn-success\">\n" +
            "  <i class=\"fa fa-refresh fa-spin\"></i>Loading\n" +
            "</button>";
        axios.get("/api/mail/send",{params: {email:email}})
            .then(function (response) {

                let data=JSON.parse(JSON.stringify(response.data));
                alert(data.message)
                console.log(response.data)
                if (data.success){
                    window.location.href="/"
                }
                else
                location.reload()
            })
    } else alert("Email noto'g'ri!");

}

function validateEmail(email)
{
    let re = /\S+@\S+\.\S+/;
    return re.test(email);
}


function checkSize() {
    let uploadField = document.getElementById("file");

    uploadField.onchange = function() {
        if(this.files[0].size > 1000000){
            this.value = "";
            return false;
        }
    }
    return true;
}

function sendFile() {

    let formData=new FormData();

    let email=document.getElementById("email").value;
    let code_string=document.getElementById("code_string").value;
    let code_html=document.getElementById("code_html").value;
    let dors="1";
    if (document.getElementById("inlineRadio1").checked){
        dors="1";
    } else dors="2";

    if (checkSize()){
        if (email.length>0&&code_string.length>0&&code_html.length>0&&document.getElementById("file").files[0]!==undefined){
            formData.append("file",document.getElementById("file").files[0]);
            formData.append("email",email)
            formData.append("code_string",code_string)
            formData.append("code_html",code_html)
            formData.append("dors",dors)
            console.log(formData)

            document.getElementById("button2").innerHTML="<button class=\"buttonload btn btn-success\">\n" +
                "  <i class=\"fa fa-refresh fa-spin\"></i>Loading\n" +
                "</button>";

            axios.post("/api/attachment/auth_save",formData,{headers:{'Content-Type':'multipart/form-data'}})
                .then(function (response) {
                    let data=JSON.parse(JSON.stringify(response.data));

                    alert(data.message);
                    location.reload();
                })
        }
        else {
            alert("Formalarni to'ldiring!")
            location.reload();
        }
    }
    // console.log("Jonibek : "+document.getElementById("file").files[0])




}


function saveFile() {

}
