// xem thêm
    let currentPage = 0 ;
    function loadMore()
    {


        currentPage++;
        fetch(`/api/donation_post/get?page=${currentPage}`)
            .then(response => response.text())
            .then(data =>   {
                const parser = new DOMParser();
                const htmlDoc = parser.parseFromString(data, 'text/html');

                // lấy các phần tử mới , ok?
                const newItems = htmlDoc.querySelector('#items-container').innerHTML;
                //thêm phần tử mới mà không xóa phần tu cũ nha !!
                //insertAdjacentHTML  là chèn phần tử HTML vào DOM mà không làm ảnh hưởng các phần tử khác
                //beforeend là sau phần tử con
                document.querySelector('#items-container').insertAdjacentHTML('beforeend', newItems);

                //cap nhat nut 'XEM THEM '
                const loadMoreButton = htmlDoc.querySelector('#load-more-container').innerHTML;
                document.querySelector('#load-more-container').innerHTML = loadMoreButton;

                uppdateProgressBars();
            })
            .catch(error => console.error('ERROR:', error));
    }
// xem thêm

//hiện thanh tiền
    function uppdateProgressBar(currentAmount, goalAmount, progressBar)
    {
        var percentAmount = (currentAmount / goalAmount) * 100;
        progressBar.style.width = percentAmount + "%";

    }
    function uppdateProgressBars(){
        let progressBars = document.querySelectorAll('.progress-container');
        progressBars.forEach(progressContainer   =>{
            let progressBar = progressContainer.querySelector(".progress-bar");

        // Lấy giá trị hiện tại và mục tiêu
        let currentAmountText = progressContainer.querySelector(".current-Amount").innerText.replace(/\./g, '').replace('đ', '').trim();
        let goalAmountText = progressContainer.querySelector(".goal-Amount").innerText.replace(/\./g, '').replace('đ', '').trim();

        let currentAmount = parseInt(currentAmountText) || 0;
        let goalAmount = parseInt(goalAmountText) || 0;

        // Cập nhật giá trị theo định dạng
        progressContainer.querySelector(".current-Amount").innerText = currentAmount.toLocaleString('vi-VN') + "đ";
        progressContainer.querySelector(".goal-Amount").innerText = goalAmount.toLocaleString('vi-VN') + "đ";

            uppdateProgressBar(currentAmount, goalAmount, progressBar);
        })
    }
    uppdateProgressBars();

//    let currentAmount = parseInt(document.getElementById("current-Amount").innerText);
//    let goalAmount = parseInt(document.getElementById("goal-Amount").innerText);
//
//    uppdateProgressBar(currentAmount, goalAmount);
//hiện thanh tiền

//chạy số
document.addEventListener('DOMContentLoaded',function (){
    const donationSuccess = document.getElementById('donationSuccess');
    const goalAmount = document.getElementById('goalAmount');
    const subscription = document.getElementById('subscription');


    const numberDonationSuccess = parseInt(donationSuccess.innerText);
    donationSuccess.innerText = 0;

    const numberGoalAmount = parseInt(goalAmount.innerText);
    numberGoalAmount.innerText = 0;

    const numberSubscription = parseInt(subscription.innerText);
    numberSubscription.innerText = 0;



    let currentNumberDonation = 0;
    let currentNumberGoalAmount = 0;
    let currentNumberSubscription = 0;
    const increment =  Math.ceil(numberDonationSuccess / 100 );

    const increment1 =  Math.ceil(numberGoalAmount / 100 );

    const increment2 =  Math.ceil(numberSubscription / 100 );

        const uppdateNumber = setInterval( function(){
            if(currentNumberDonation < numberDonationSuccess)
            {
                currentNumberDonation += increment;
                if(currentNumberDonation > numberDonationSuccess){
                    currentNumberDonation = numberDonationSuccess;
                }
                donationSuccess.innerText = currentNumberDonation;
            }else{
                clearInterval(uppdateNumber);
            }
        },30)
         const uppdateNumberGoalAmount = setInterval( function(){
             if(currentNumberGoalAmount < numberGoalAmount)
             {
                 currentNumberGoalAmount += increment1;
                 if(currentNumberGoalAmount > numberGoalAmount){
                     currentNumberGoalAmount = numberGoalAmount;
                 }
                 goalAmount.innerText = currentNumberGoalAmount +"+ Tỷ";
             }else{
                 clearInterval(uppdateNumberGoalAmount);
             }
         },50)
         const uppdateNumberSubscription = setInterval( function(){
             if(currentNumberSubscription < numberSubscription)
             {
                 currentNumberSubscription += increment2;
                 if(currentNumberSubscription > numberSubscription){
                     currentNumberSubscription = numberSubscription;
                 }
                 subscription.innerText = currentNumberSubscription + "+ triệu";
             }else{
                 clearInterval(uppdateNumberSubscription);
             }
         },50)
})

//chạy số

// JavaScript để thêm class 'visible' khi cuộn tới phần tử
window.addEventListener('scroll', function() {
    const elements = document.querySelectorAll('.gt'); // Lấy tất cả các phần tử với class 'gt'

    const donations = document.querySelectorAll('.donationPost');

    elements.forEach(element => {
        // Kiểm tra vị trí của phần tử so với cửa sổ trình duyệt
        if (element.getBoundingClientRect().top < window.innerHeight) {
            element.classList.add('visible'); // Thêm class 'visible' khi phần tử vào trong cửa sổ
        }else{
             element.classList.remove('visible');
        }
    });
    let delay = 0;
    donations.forEach(donation => {
        if (donation.getBoundingClientRect().top < window.innerHeight) {
            setTimeout( () =>{
                donation.classList.add('visible');
            },delay)
            delay+=10;
        }else{
             donation.classList.remove('visible');
        }
    });
});


