document.querySelector("#calcform").addEventListener("submit", async(e) =>{
    e.preventDefault();

    const form = e.currentTarget;
    // 백엔드 주소
    const backend = e.currentTarget.getAttribute("action");

    // 응답 결과가 저장될 객체
    let response = null;

    // 선택된 연산자만 추출
    const op = form.querySelector('select[name="operator"]').value;

    // 가상의 Form 데이터를 생성하여 Axios로 전송
    const formData = new FormData(form);

    try {
        // 연산자에 따라 GET,POST,PUT,DELETE 선택
        // 전송 방식에 따라 axios에서 파라미터를 처리하는 방법이 다름

        switch (op){
            case "+" :
                response = await axios.get(backend, {
                // GET 방식으로 전송할 때는 params 속성을 사용하여 JSON 객체로 구성해야 함
                params : Object.fromEntries(formData)
            });
            break;
            case "-" :
                response = await axios.post(backend, formData);
            break;

            case "*" :
                response = await axios.put(backend, formData);
            break;

            case "/" :
                response = await axios.delete(backend, {
                // DELETE 방식으로 전송할 때는 data 속성을 사용하여 JSON 객체로 구성해야 함
                data : formData
            });
            break;
        }
    } catch (error) {
        let alertMsg = null;
        console.log(error);

        // SpringBoot로부토 전달받은 에러 메세지가 있다면?
        if (error.response?.data) {
            const data = error.response.data;

            // 메세지 창에 표시할 내용
            alertMsg = data.message;

            // 백앤드에서 발생한 상세 에러 내용을 브라우저 콘솔에 출력
            // --> 이 코드는 보안에 취약할 수 있으므로 실제 서비스에서는 제거할 것
            console.error("[Error occurred in backend server");
            console.error(`[${data.status}] ${data.error}`);
            console.error(data.trace);
        } else {
            // SpringBoot로부터 전달받은 에러 메세지가 없다면?
            // --> Axios에서 전달받은 에러 메세지를 그대로 표시

            // 메세지 창에 표시할 내용
            alertMsg = error.message;

            // Axios의 기본 에러 메세지를 추출하여 문자열로 구성
            console.error("[Error occurred in Axios]");
            
            console.error(`[${data.code}] ${error.message}`);
        }

        // 메세지 박스로 에러 내용 표시
        alert(alertMsg);

        // catch에서 리턴을 하더라도 finally가 수행된 후 실행이 취소됨
        return;
    }

    // 결과 처리
    const result = response.data;
    
    //json 결과에 호출된 x,y result를 사용하여 문자열을 받은 후 li 태그를 출력
    const li = document.createElement('li');
    li.textContent = `${result.x} ${op} ${result.y} = ${result.result}`;
    document.querySelector('#result').appendChild(li);
});