// document.write("<script src='//dapi.kakao.com/v2/maps/sdk.js?appkey=21fe492d0acd4e2194c4448b9796bb1c\"></script>);
// <script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=21fe492d0acd4e2194c4448b9796bb1c"></script>

const imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png";
// 마커 이미지의 이미지 크기 입니다
const imageSize = new kakao.maps.Size(24, 35);
// 마커 이미지를 생성합니다
const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);
let mapContainer = document.getElementById('map'), // 지도를 표시할 div
    mapOption = {
        center: new kakao.maps.LatLng(37.57298619696376, 126.99830259588441), // 지도의 중심좌표
        level: 10 // 지도의 확대 레벨
    };
let map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

// var_desc 로드맵
let roadviewContainer = document.getElementById('roadview'); //로드뷰를 표시할 div
let roadview = new kakao.maps.Roadview(roadviewContainer); //로드뷰 객체
let roadviewClient = new kakao.maps.RoadviewClient(); //좌표로부터 로드뷰 파노ID를 가져올 로드뷰 helper객체


// 마커를 표시할 위치와 title 객체 배열입니다
let positions = [];
let markers = [];


<!--section 모달 탭 클릭 시 동작하는 함수-->
$(".tabs").click(function () {
    $(".tabs").removeClass("active");
    $('.tabs h6').removeClass("font-weight-bold");
    $(".tabs h6").addClass("text-muted");
    $(this).children("h6").removeClass("text-muted");
    $(this).children("h6").addClass("font-weight-bold");
    $(this).addClass("active");

    current_fs = $(".active");

    next_fs = $(this).attr('id');
    next_fs = "#" + next_fs + "1";

    $("fieldset").removeClass("show");
    $(next_fs).addClass("show");

    current_fs.animate({}, {
        step: function () {
            current_fs.css({
                'display': 'none',
                'position': 'relative'
            });
            next_fs.css({
                'display': 'block'
            });
        }
    });
});

function hideModal() {
    $('#myModal').on('hidden.bs.modal', function (e) {
        // do something...
    })
}

<!--section 시대 토글 버튼 클릭시 동작하는 함수-->
function ccceToggle() {
    let x = document.getElementById("checkbox-group");
    if (x.style.display === "none") {
        x.style.display = "block";
    } else {
        x.style.display = "none";
    }
}

// desc 열람 완료 작업
// function addToReadingList() {
//     console.log("addToReadingList")
//     $.ajax({
//         url: "addToReadingList",
//         // dataType: "json",
//         contentType: "application/json; charset=utf-8;",
//         type: "POST",
//         data: JSON.stringify({
//             title: $('.modal-heritage-title-public').text(),
//             place: $('.modal-ccbaLcad-public').text()
//         }),
//         success: function (result) {
//             alert("열람 완료")
//
//
//         },
//         error: function (result) {
//             // alert('error 열람');
//             console.log('error 열람');
//         }
//     });
// }

// desc 지도 레벨을 변경하는 함수

// 지도 레벨을 표시합니다
// displayLevel();

// 지도 레벨은 지도의 확대 수준을 의미합니다
// 지도 레벨은 1부터 14레벨이 있으며 숫자가 작을수록 지도 확대 수준이 높습니다
// desc 지도 확대
function zoomIn() {
    // 현재 지도의 레벨을 얻어옵니다
    let level = map.getLevel();

    // 지도를 1레벨 내립니다 (지도가 확대됩니다)
    map.setLevel(level - 1);

    // 지도 레벨을 표시합니다
    // displayLevel();
}

// desc 지도 축소
function zoomOut() {
    // 현재 지도의 레벨을 얻어옵니다
    let level = map.getLevel();

    // 지도를 13레벨(전체)로 보여준다. (지도가 축소됩니다)
    map.setLevel(13);

    // 지도 레벨을 표시합니다
    // displayLevel();
}

// desc 지도 부드럽게 이동
function panTo(default_lat = 36, default_lon = 127.5) {
    // let default_lat = 36, default_lon = 127.5
    // 이동할 위도 경도 위치를 생성합니다
    var moveLatLon = new kakao.maps.LatLng(default_lat, default_lon);

    // 지도 중심을 부드럽게 이동시킵니다
    // 만약 이동할 거리가 지도 화면보다 크면 부드러운 효과 없이 이동합니다
    map.panTo(moveLatLon);
}

// function displayLevel(){
//     var levelEl = document.getElementById('maplevel');
//     levelEl.innerHTML = '현재 지도 레벨은 ' + map.getLevel() + ' 레벨 입니다.';
// }


let default_lat = 37.57298619696376, default_lon = 126.99830259588441

// desc 지도 불러오는 로직
function displayMap(lat = default_lat, lon = default_lon) { // 위치 변경 후 display
    mapContainer = document.getElementById('map'), // 지도를 표시할 div
        mapOption = {
            center: new kakao.maps.LatLng(lat, lon), // 지도의 중심좌표
            level: 8 // 지도의 확대 레벨
        };
    map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다
}

// desc 지도에서 마커 숨기기
function hideAllMarkers() {
    mapContainer = document.getElementById('map'), // 지도를 표시할 div
        mapOption = {
            center: new kakao.maps.LatLng(36, 127.5), // 지도의 중심좌표
            level: 13 // 지도의 확대 레벨
        };
    map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다
    map = new kakao.maps.Map(mapContainer, mapOption);
    markers = [];
}

<!--modal 닫는 리스너-->
$("#close-button").on("click", function (e) {
    e.preventDefault();
    $('#myModal').modal('hide')
    $(".carousel-inner").text("")

})

// desc 배열에 추가된 마커들을 지도에 표시하거나 삭제하는 함수입니다
function setMarkers(map) {
    for (let i = 0; i < markers.length; i++) {
        markers[i].setMap(map);
    }
}

let duration = 0
let start = 0

function addListenerToMarker(marker) {
    kakao.maps.event.addListener(marker, 'click', function () {

        // 모달 제목
        $('.modal-heritage-title-public').text(marker.getTitle());

        let pos = marker.getPosition();
        $('#myModal').modal('show');
        $('#myModal').modal({keyboard: false})
        $('#myModal').modal('toggle')
        $('#myModal').modal('handleUpdate')
        // let flag = false

        $("#myModal").on('shown.bs.modal', function () {
            // alert("modal창 열림 감지!");
            // console.log("modal창 열림 감지!")
            $(this).off('shown.bs.modal')
            start = new Date();

        });
        $('#myModal').on('hidden.bs.modal', function (e) {
            // console.log("modal창 닫힘 감지!")

            // alert("modal창 닫힘 감지!")
            let end = new Date();
            duration = end - start
            // console.log("end = " + end);
            // console.log("duration = " + duration);
            $(this).off('hidden.bs.modal')

            saveReadingTime(duration / 1000)


        })
        moveToDetailPage(marker.getTitle())
        let position = marker.getPosition()
        roadviewClient.getNearestPanoId(position, 50, function (panoId) {
            if (panoId) {
                roadview.setPanoId(panoId, position); //panoId와 중심좌표를 통해 로드뷰 실행
            }
        });

    });
}

// desc 지도에 마커와 인포윈도우를 표시하는 함수입니다
function displayMarker(locPosition, message) {

    // 마커를 생성합니다
    let marker = new kakao.maps.Marker({
        map: map,
        position: locPosition
    });


    // 지도 중심좌표를 접속위치로 변경합니다
    map.setCenter(locPosition);
}

let count = 0;

// function getMoreTimeInfo() {
//     let ccce = $(".modal-ccceName-public").text()
//     console.log("getMoreTimeInfo")
//     console.log(ccce)
//     $.ajax({
//         url: "timeInfo",
//         // contentType: "application/json; charset=utf-8;",
//         type: "GET",
//         success: function (result) {
//             console.log("getMoreTimeInfo")
//             $.each(result, function (index, obj) {
//                 console.log(obj)
//
//             });
//
//
//         }
//     })
//
// }

// $("#more-time-info").on("click", function (e) {
//     e.preventDefault();
//     $.ajax({
//         url: "getMoreTimeInfo",
//         contentType: "application/json; charset=utf-8;",
//         type: "POST",
//         data: {},
//         success: function (result) {
//
//
//             $.each(result, function (index, obj) {
//
//
//             });
//
//
//         }
//     })
// })

$("#more-time-info").on("click", function (e) {
    let ccce = $('.modal-ccceName-public').html();
    console.log(ccce)
    let url = '/getMoreTimeInfo?ccce=' + ccce
    // $('#more-time-info').href('')
    location.replace(url)
    // $(location).attr("href", url);

})
// '내 위치' 버튼 클릭
$("#here").on("click", function (e) {
    e.preventDefault();
    $('h2 span').text("");

    count = 50;
    if (navigator.geolocation) {

        // GeoLocation을 이용해서 접속 위치를 얻어옵니다
        navigator.geolocation.getCurrentPosition(function (position) {

            let lati = position.coords.latitude; // 위도
            let longi = position.coords.longitude; // 경도


            let locPosition = new kakao.maps.LatLng(lati, longi), // 마커가 표시될 위치를 geolocation으로 얻어온 좌표로 생성합니다
                message = '<div style="padding:5px;">내 위치</div>'; // 인포윈도우에 표시될 내용입니다

            // 마커와 인포윈도우를 표시합니다
            displayMarker(locPosition, message);

            $.ajax({
                url: "getCurPos",
                dataType: "json",
                contentType: "application/json; charset=utf-8;",
                type: "POST",
                data: JSON.stringify({
                    lat: lati,
                    lon: longi
                }),
                success: function (result) {

                    hideAllMarkers();
                    panTo(lati, longi);
                    mapContainer = document.getElementById('map'), // 지도를 표시할 div
                        mapOption = {
                            center: new kakao.maps.LatLng(lati, longi), // 지도의 중심좌표
                            level: 4 // 지도의 확대 레벨
                        };
                    map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다
                    $('h2 span').text("현재 위치와 가까운 문화재가 " + result.length + "개 검색되었습니다.")
                    $('h1 span').text("")
                    // $('#search-keyword').attr('placeholder', "문화재 이름이나 시대를 검색해보세요.") // Todo: placehodler 바꾸려 했으나 잘 안됨
                    $("#search-keyword").attr("placeholder", "문화재 이름이나 시대를 검색해보세요.").val("").focus().blur();
                    positions = []
                    markers = []

                    $.each(result, function (index, obj) {

                        const pos = {
                            "title": obj.ccbaMnm1, // 이름 작성
                            "latlng": new kakao.maps.LatLng(obj.latitude, obj.longitude) // 위도, 경도 작성
                        }
                        positions.push(pos)
                    });

                    for (let i = 0; i < positions.length; i++) {
                        // 마커를 생성합니다
                        let marker = new kakao.maps.Marker({
                            map: map, // 마커를 표시할 지도
                            position: positions[i].latlng, // 마커를 표시할 위치
                            title: positions[i].title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
                            image: markerImage, // 마커 이미지
                            clickable: true // 마커를 클릭했을 때 지도의 클릭 이벤트가 발생하지 않도록 설정합니다.
                        });
                        marker.setMap(map)
                        addListenerToMarker(marker)

                        markers.push(marker);

                        //desc 백그라운드에서 계속 불러오기

                    } // for end
                    getCurPosInBackground(lati, longi);

                },
                error: function (result) {
                    // alert('error getCurPos');
                    console.log('error getCurPos');
                }
            });

        });

    } else { // HTML5의 GeoLocation을 사용할 수 없을때 마커 표시 위치와 인포윈도우 내용을 설정합니다

        let locPosition = new kakao.maps.LatLng(33.450701, 126.570667),
            message = 'geolocation을 사용할수 없어요..'

        displayMarker(locPosition, message);
    }


});

function getCurPosInBackground(lati, longi) {
    console.log(getCurPosInBackground)
    $.ajax({
        url: "getCurPosInBackground",
        dataType: "json",
        contentType: "application/json; charset=utf-8;",
        type: "POST",
        data: JSON.stringify({
            lat: lati,
            lon: longi
        }),
        success: function (result) {
            count = count + result.length;

            // desc 결과가 더이상 없을 때까지 재귀함수로 호출해보자
            // if (result.length === 0) {
            //     return;
            // }
            if (count > 1050) {
                return;
            }
            // console.log(result.length)
            // console.log("추가 "+result.length+"개 성공");
            $('h2 span').text("현재 위치와 가까운 문화재가 " + result.length + "개 추가로 검색되었습니다. (" + count +" / 1050)")

            $('h1 span').text("")
            // $('#search-keyword').attr('placeholder', "문화재 이름이나 시대를 검색해보세요.") // Todo: placehodler 바꾸려 했으나 잘 안됨
            $("#search-keyword").attr("placeholder", "문화재 이름이나 시대를 검색해보세요.").val("").focus().blur();

            positions = []
            markers = []

            $.each(result, function (index, obj) {

                const pos = {
                    "title": obj.ccbaMnm1, // 이름 작성
                    "latlng": new kakao.maps.LatLng(obj.latitude, obj.longitude) // 위도, 경도 작성
                }
                positions.push(pos)
            });

            for (let i = 0; i < positions.length; i++) {
                // 마커를 생성합니다
                let marker = new kakao.maps.Marker({
                    map: map, // 마커를 표시할 지도
                    position: positions[i].latlng, // 마커를 표시할 위치
                    title: positions[i].title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
                    image: markerImage, // 마커 이미지
                    clickable: true // 마커를 클릭했을 때 지도의 클릭 이벤트가 발생하지 않도록 설정합니다.
                });
                marker.setMap(map)
                addListenerToMarker(marker)

                markers.push(marker);

            } // for end

            getCurPosInBackground(lati, longi) // desc 재귀함수

        },
        error: function (result) {
            // alert('error getCurPos');
            console.log('error getCurPos');
        }
    });

}

// desc 지도 불러오는 로직
// desc 버튼 클릭 시 시대별 display
function displayByCcce() {
    // console.log("displayByCcce")
    hideAllMarkers();

    $.ajax({
        url: "displayByCcce",
        type: "GET",
        data: '',
        contentType: "application/json; charset=utf-8;",
        success: function (data) {
            // console.log("displayByCcce data")

            positions = []
            markers = []
            let lati = 0, longi = 0;

            $.each(data, function (index, obj) {
                const pos = {
                    "title": obj.ccbaMnm1, // 이름 작성
                    "latlng": new kakao.maps.LatLng(obj.latitude, obj.longitude) // 위도, 경도 작성
                }
                lati += obj.latitude
                longi += obj.longitude
                positions.push(pos)
            })

            for (let i = 0; i < positions.length; i++) {
                // 마커를 생성합니다
                let marker = new kakao.maps.Marker({
                    map: map, // 마커를 표시할 지도
                    position: positions[i].latlng, // 마커를 표시할 위치
                    title: positions[i].title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
                    image: markerImage, // 마커 이미지
                    clickable: true // 마커를 클릭했을 때 지도의 클릭 이벤트가 발생하지 않도록 설정합니다.
                });
                addListenerToMarker(marker)
                markers.push(marker);
            } // for end

            zoomOut(); // desc 시대 버튼 - 지도 축소
            panTo() // desc 중심 좌표를 부드럽게 이동
        },
        error: function () {
            console.log("err display");
        }
    });

}

// // desc 지도 불러오는 로직
// function selectHeritages() {
//     hideAllMarkers();
//
//     $.ajax({
//         url: "heritageList",
//         type: "GET",
//         data: '',
//         contentType: "application/json; charset=utf-8;",
//         dataType: "json",
//         success: function (data) {
//
//             $.each(data, function (index, obj) {
//                 const pos = {
//                     "title": obj.ccbaMnm1, // 이름 작성
//                     "latlng": new kakao.maps.LatLng(obj.latitude, obj.longitude) // 위도, 경도 작성
//                 }
//                 positions.push(pos)
//                 markers.push(marker);
//             })
//
//
//             for (let i = 0; i < positions.length; i++) {
//                 // 마커를 생성합니다
//                 let marker = new kakao.maps.Marker({
//                     map: map, // 마커를 표시할 지도
//                     position: positions[i].latlng, // 마커를 표시할 위치
//                     title: positions[i].title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
//                     image: markerImage, // 마커 이미지
//                     clickable: true // 마커를 클릭했을 때 지도의 클릭 이벤트가 발생하지 않도록 설정합니다.
//                 });
//                 markers.push(marker);
//                 addListenerToMarker(marker)
//
//
//             } // for end
//
//         },
//         error: function () {
//             // alert("error heritageList");
//             console.log("error heritageList");
//         }
//     });
// }
let photo_flag = false;

// desc 마커 클릭시 상세 페이지 모달창으로 넘어가는 로직
function moveToDetailPage(title) {
    $.ajax({
        type: "POST",
        url: "getThinkbigHeritageInfo",
        // dataType: "json",

        data: {
            title: title
        },
        success: function (result) {
            // Todo: 웅진백과 결과 없으면? 정확도?
            if (result.length === 0) { // desc 웅진백과 결과 없으면
                $(".thinkbig-fieldset").hide();

                // console.log("1")
                photo_flag = true
                //desc 내용 비우기
                $(".modal-title-thinkbig").text("")
                $(".carousel-inner").text("")
                $(".modal-content-thinkbig").text("")
                $("#modal-tags-thinkbig").text("")

                // desc 방법1
                //$(".thinkbig-fieldset").hide();

                // $("#tab01").hide();
                // $("#tab011").hide();
                // $("#tab02").children("h6").removeClass("text-muted");
                // $("#tab02").children("h6").addClass("font-weight-bold");
                // $("#tab02").addClass("active");

                // desc 방법2: default를 문화재청으로
                $(".tabs").removeClass("active");
                $(".tabs h6").removeClass("font-weight-bold");
                $(".tabs h6").addClass("text-muted");
                $("#tab02").children("h6").removeClass("text-muted");
                $("#tab02").children("h6").addClass("font-weight-bold");
                $("#tab02").addClass("active");

                $("fieldset").removeClass("show");
                $("#tab021").addClass("show");

                $("#tab01").removeClass("container-able"); // desc 클릭 불가 지우기

                $("#tab01").addClass("container-disable"); // desc 클릭 가능 추가



            } else { // desc 웅진백과 결과 잇으면
                photo_flag = false

                $("#tab01").removeClass("container-disable"); // desc 클릭 가능을 지우기
                $("#tab01").addClass("container-able"); // desc 클릭 불가


                $(".thinkbig-fieldset").show();

                $(".tabs").removeClass("active");
                $(".tabs h6").removeClass("font-weight-bold");
                $(".tabs h6").addClass("text-muted");
                $("#tab01").children("h6").removeClass("text-muted");
                $("#tab01").children("h6").addClass("font-weight-bold");
                $("#tab01").addClass("active");

                current_fs = $(".active");


                $("fieldset").removeClass("show");
                $("#tab011").addClass("show");

                current_fs.animate({}, {
                    step: function () {
                        current_fs.css({
                            'display': 'none',
                            'position': 'relative'
                        });
                        next_fs.css({
                            'display': 'block'
                        });
                    }
                });

                $('.modal-content-thinkbig').html(result.content);

            }


            // desc 태그 가져오는 비동기처리
            $.ajax({
                type: "POST",
                url: "getThinkbigTag",
                data: {
                    title: title
                },
                success: function (result) {

                    let text = ""

                    $.each(result, function (index, keyword) {
                        text += '<h5><span id="modal-tags-thinkbig" class="badge badge-warning badge-secondary" style="margin-right: 3px"> #' + keyword + '</span></h5>'
                    })
                    $('#modal-tags-thinkbig').html(text)

                },
                error: function (result) {
                    console.log('error getThinkbigTag');
                }
            });

            // desc 사진들 가져오는 비동기처리

            $.ajax({
                type: "POST",
                url: "getThinkbigPhotos",
                data: {
                    title: title
                },
                success: function (result) {
                    if (photo_flag)//백과 결과 없으면
                    {
                        $('.carousel-inner').html()
                        return;
                    }

                    // console.log("getThinkbigPhotos");
                    let text = ""

                    if (result.length === 0) {
                        text = '<img class="w-100" src="../../image/noimage.png" alt="no_image">'
                        $('.carousel-inner').html(text)
                        return;
                    }


                    $.each(result, function (index, imgUrl) {
                        if (index === 0) {
                            text += '<div class="carousel-item active"><img class="w-100" src="' + imgUrl + '" alt="image"> </div>'
                        } else {
                            text += '<div class="carousel-item"><img class="w-100" src="' + imgUrl + '" alt="image"> </div>'

                        }
                        // console.log(imgUrl)

                    })
                    $('.carousel-inner').html(text)

                },
                error: function (result) {
                    console.log('error getThinkbigPhotos');
                }
            });


            // desc 문화재청 tab 정보
            $.ajax({
                type: "POST",
                url: "getHeritageInfo",
                data: {
                    title: title
                },
                success: function (result) {
                    // console.log("getHeritageInfo")
                    // Todo: 공공데이터 탭에 보여줄 내용 파싱해오기, Null 처리 해주기

                    if (result.length === 0) {
                        alert("정보가 없습니다.")
                    }
                    // console.log("result.imageUrl")
                    // console.log(result.imageUrl)
                    if (result.imageUrl === "" || result.imageUrl === null) {
                        console.log("img null")
                        $('.modal-img-public').html("이미지 정보가 없습니다.");
                    } else {
                        let text = "<img src=\"" + result.imageUrl + "\"/>"
                        $('.modal-img-public').html(text);

                    }
                    if (result.ccceName === "") { // 시대가 없으면 연표정보 더보기 숨기기

                        $("#more-time-info").hide();
                    } else {
                        $("#more-time-info").show();

                    }
                    $('.modal-ccbaLcad-public').html(result.ccbaLcad);
                    $('.modal-ccmaName-public').html(result.ccmaName);
                    $('.modal-ccceName-public').html(result.ccceName); // 시대
                    $('.modal-content-public').html(result.content);

                    getPublicDataTag(result.no); // desc 태그 가져오기 - 비동기처리

                    if (result.ccceName !== "") { // desc 시대쪽 정보가 있을 경우
                        console.log("시대 정보 있음")
                        console.log(result.ccceName)

                        // 시대쪽 태그 보이기
                        $(".time-table").show();

                        getTimeInfoByCcceName(result.ccceName) // desc 이 시기 연표 정보 가져오기 - 비동기처리
                        // console.log("before getMoreTimeInfo")
                        // getMoreTimeInfo(result.ccceName)
                    } else { // // desc 시대쪽 정보가 없는 경우
                        console.log("시대 정보 없음")
                        console.log(result.ccceName)

                        // 시대쪽 태그 숨기기
                        $(".time-table").hide();


                    }

                },
                error: function (result) {
                    console.log('error getHeritageInfo');
                }
            });

        },
        error: function (result) {
            console.log('error getThinkbigHeritageInfo');
        }
    });

}

// desc 이 시기 연표 정보 가져오기 - 비동기처리
function getTimeInfoByCcceName(ccceName) {
    $.ajax({
        type: "POST",
        url: "getTimeInfoByCcceName",
        data: {
            ccceName: ccceName
        },
        success: function (result) {
            // console.log("getTimeInfoByCcceName")
            console.log(result)
            if (result.length === 0) {
                $('.time-table').hide();
                return;
            }
            console.log("1")

            $('.time-table').show();
            console.log("2")

            let text = ''
            $.each(result, function (index, obj) {

                text += '<tr class="m-0"><td class="w-8">' + obj.time + '</td><td class="w-20">' + obj.timeTitle + '</td><td class="w-30">' + obj.timeDscr + '</td></tr>' // 시기, 사건, 설명 넣으면 됨

            })
            console.log("3")

            $('.time-table-tbody').html(text);
            console.log("4")

        },

        error: function (request, status, error) {
            console.log("error getTimeInfoByCcceName")
            console.log("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
        }
    });
}

// desc 문화재청 태그
function getPublicDataTag(no) {

    $.ajax({
        type: "POST",
        url: "getPublicDataTag",
        data: {
            no: no
        },
        success: function (result) {
            // console.log("getPublicDataTag")

            let text = ""
            $.each(result, function (index, obj) {

                text += '<h5><span id="modal-tags-public" class="badge badge-warning badge-secondary" style="margin-right: 3px"> #' + obj + '</span></h5>'
            })

            $('#modal-tags-public').html(text)

        },
        error: function (result) {
            console.log('error publicData');
        }
    });

}

// desc 시대 이름 버튼 클릭
$(".ccce-btn").on("click", function (e) {
    e.preventDefault();
    $('h2 span').text("");
    // $('.search-keyword').attr('placeholder', "문화재 이름이나 시대를 검색해보세요.") // Todo: placehodler 바꾸려 했으나 잘 안됨
    $("#search-keyword").attr("placeholder", "문화재 이름이나 시대를 검색해보세요.").val("").focus().blur();

    hideAllMarkers();

    $.ajax({
        type: "POST",
        url: "getCcce",

        data: {
            ccce: $(this).val()
        },
        success: function () {
            // console.log("시대 이름 버튼 클릭")
            displayByCcce();
        },
        error: function (request, status, error) {
            console.log("error getCcce")
            console.log("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
        }
    });

});

// desc 검색 버튼 클릭
$(".search-btn").on("click", function (e) {

    e.preventDefault();
    hideAllMarkers();

    $.ajax({
        type: "POST",
        url: "getKeyword",
        dataType: "json",

        data: {
            keyword: $('#search-keyword').val()
        },
        success: function (data) {

            $('h2 span').text("'" + $('#search-keyword').val() + "'" + " 검색 결과가 " + data.length + "개 검색되었습니다.")
            $('h1 span').text("")

            // 검색 키워드 결과 null일 때 처리
            if (data.length === 0) {
                alert("결과가 없습니다");
                return;
            }

            positions = []
            markers = []
            let lati, longi;
            $.each(data, function (index, obj) {
                if (index === 0) {
                    lati = obj.latitude
                    longi = obj.longitude
                }

                const pos = {
                    "title": obj.ccbaMnm1, // 이름 작성
                    "latlng": new kakao.maps.LatLng(obj.latitude, obj.longitude) // 위도, 경도 작성
                }
                positions.push(pos)
            })

            hideAllMarkers();
            panTo(lati, longi);

            mapContainer = document.getElementById('map'), // 지도를 표시할 div
                mapOption = {
                    center: new kakao.maps.LatLng(lati, longi), // 지도의 중심좌표
                    level: 4 // 지도의 확대 레벨
                };
            map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

            for (let i = 0; i < positions.length; i++) {
                // 마커를 생성합니다
                let marker = new kakao.maps.Marker({
                    map: map, // 마커를 표시할 지도
                    position: positions[i].latlng, // 마커를 표시할 위치
                    title: positions[i].title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
                    image: markerImage, // 마커 이미지
                    clickable: true // 마커를 클릭했을 때 지도의 클릭 이벤트가 발생하지 않도록 설정합니다.
                });

                addListenerToMarker(marker)

                markers.push(marker);
            } // for end
        },
        error: function (result) {
            console.log('error getKeyword');
        }
    });
});


// desc 마커에 리스너 부착


function saveReadingTime(duration) {
    // console.log("saveReadingTime: " + duration)

    $.ajax({
        type: "POST",
        url: "saveReadingTime",
        // dataType: "json",
        data: {
            duration: duration,
            title: $('.modal-heritage-title-public').text(),
            place: $('.modal-ccbaLcad-public').text()
        },
        success: function (data) {

            $.each(data, function (index, obj) {

            })


        },
        error: function (result) {
            console.log('error getKeyword');
        }
    });
}