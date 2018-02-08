# EXPLOG

## 기간

2017.11.27 ~ 2018.01.19

## 역할

- 전체적인 총괄, MVP Architecture 설계
- Splash, SignIn, NewsPeed, Cover, Post, Search Layout 및 기능 구현
- Retrofit2 + RxJava 적용 및 OkHTTP3(Interceptor) 적용

## 소개

`EXPLOG` 는 Explore + Log 의 합성어로 자신의 추억이 담긴 여행 내용을 기록하고, 남들과 공유하는 플랫폼입니다.

## 개발 환경

-	개발 언어: Java
-	개발 환경: JDK 1.8, SDK(Min 16, Target 23)
-	서버: Python-django (참고: https://gangbok119.gitbooks.io/explog-api/content/)
-	개발 도구: Android Studio 3.0

## 프로젝트 관리

-	개발 프로세스 : Agile/Scrum 방법론을 통해 개발
-	문서화 : Google 스프레드 시트를 통해 관리
-	Communication : Slack 을 통한 팀 대화
-	소스 관리 : GitHub 로 관리
-	회의 : 일일 미팅 후 이슈 사항 및 리뷰 작성, 주간 회고

## 화면

## 사용 Skills

1. MVP Architecture 적용

    - Google Android MVP Architecture를 참고하여 적용[[Todo-MVP]](https://github.com/googlesamples/android-architecture/tree/todo-mvp/)

    1. MVP interface 작성[[소스코드]](https://github.com/Hooooong/_Explog/blob/master/app/src/main/java/com/hongsup/explog/view/post/contract/PostContract.java)

        - Presenter 또는 Contract interface 작성이 있지만 Android MVP Architecture를 참고하여 Contract 작성

        ```java
        public interface PostContract {
          // View 에 관련된 interface
          // View 에 관련된 Control(Click, Page 이동, View 관리) 을 담당한다.
          interface iView {
              View getView();

              void setPresenter(iPresenter presenter);

              // 생략
          }

          // Presenter 에 관련된 interface
          // Model 과 관련하여 Data 처리를 담당한다.
          interface iPresenter {

              void attachView(iView view);

              void loadPostContent(PostCover cover);

              // 생략
          }
        }
        ```

    2. Activity, View, Prsenter 작성

        - Activity 는 `Contract.iView` 와 `Contract.iPresenter` 를 구현한 객체를 연결하는 용도로 사용

        ```java
        public class PostActivity extends AppCompatActivity  {

            private static final String TAG = "PotsActivity";

            private PostContract.iView postView;
            private PostContract.iPresenter postPresenter;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                postView = new PostView(this);
                postPresenter = new PostPresenter();

                postPresenter.attachView(postView);
                postView.setPresenter(postPresenter);

                setContentView(postView.getView());
            }

            // 생략...
        }
        ```

        - View 는 `Contract.iView` 를 implements 한다.

        ```java
        public class PostView implements PostContract.iView {
          public PostView(Context context) {
              this.context = context;
              view = LayoutInflater.from(context).inflate(R.layout.activity_post, null);
              // 생략
          }

          @Override
          public void setPresenter(PostContract.iPresenter presenter) {
              this.presenter = presenter;
              this.presenter.loadPostContent(cover);
          }

          @Override
          public View getView() {
              return view;
          }

          // 생략...
        }
        ```

        - Preseneter 는 `Contract.iPresenter` 를 implements 하고, Model 관련 Repository 를 연결한다.

        ```java
        public class PostPresenter implements PostContract.iPresenter {

          private PostContract.iView view;
          private PostRepository repository;

          public PostPresenter() {
              // Repository 를 통해 Local 이나 Remote Data 를 통신한다.
              repository = PostRepository.getInstance();
          }

          @Override
          public void attachView(PostContract.iView view) {
              this.view = view;
          }

          // 생략...
        }
        ```

    3. Model(Repository) 작성 [[소스코드]](https://github.com/Hooooong/_Explog/tree/master/app/src/main/java/com/hongsup/explog/data/search/source)

        - Interface 작성 (Source)

        ```java
        public interface SearchSource {
            // Local 에서 사용되는 Data 를 처리하는 interface
            // 주로 SQLite, SharedPreferences 등 기기 내부 Data를 담당
            interface Local{
                List<String> loadRecentSearchWord();

                void insertSearchWord(String word);
                void deleteSearchWord(String word);
            }
            // Remote 에서 사용되는 Data 를 처리하는 interface
            // 주로 외부 HTTP 통신에 관련된 Data 를 담당
            interface Remote{

                Observable<Response<PostResult>> loadSearchResult(String word);
            }
        }
        ```

        - 직접적인 Source 관련 Class 작성 (Remote 도 동일)

        ```java      
        public class SearchLocalDataSource implements SearchSource.Local {

            private DBHelperUtil dbHelper;

            private static SearchLocalDataSource instance;

            private SearchLocalDataSource(Context context) {
                dbHelper = DBHelperUtil.getInstance(context);
            }

            public static SearchLocalDataSource getInstance(Context context) {
                if (instance == null)
                    return instance = new SearchLocalDataSource(context);
                return instance;
            }

            @Override
            public List<String> loadRecentSearchWord() {
              // 생략...
            }

            @Override
            public void insertSearchWord(String word) {
              // 생략...
            }

            @Override
            public void deleteSearchWord(String word) {
              // 생략...
            }
        }
        ```

        - Presenter 에서 사용되는 Repository 작성 (implements Source.Local, Source.Remote)

        ```java
        public class SearchRepository implements SearchSource.Local, SearchSource.Remote {

            private static SearchRepository instance;

            private SearchLocalDataSource searchLocalDataSource;
            private SearchRemoteDataSource searchRemoteDataSource;
            // Search 관련 DataSource 들을 생성
            private SearchRepository(Context context) {
                searchLocalDataSource = SearchLocalDataSource.getInstance(context);
                searchRemoteDataSource = SearchRemoteDataSource.getInstance();
            }

            // Singleton Pattern
            public static SearchRepository getInstance(Context context) {
                if (instance == null)
                    instance = new SearchRepository(context);
                return instance;
            }

            @Override
            public List<String> loadRecentSearchWord() {
                return searchLocalDataSource.loadRecentSearchWord();
            }

            @Override
            public void insertSearchWord(String word) {
                searchLocalDataSource.insertSearchWord(word);
            }

            @Override
            public void deleteSearchWord(String word) {
                searchLocalDataSource.deleteSearchWord(word);
            }

            @Override
            public Observable<Response<PostResult>> loadSearchResult(String word) {
                return searchRemoteDataSource.loadSearchResult(word);
            }
        }
        ```

2. HTTP 통신

    1. Retrofit2 와 RxJava를 활용하여 RESTful 통신 (GET, POST, DELETE, PATCH 등)

        - Retrofit2 Interface 작성 (Retrun 값을 Retrofit2 의 Call 이 아닌 RxJava 의 Observable 을 사용) [[소스코드]](https://github.com/Hooooong/_Explog/tree/master/app/src/main/java/com/hongsup/explog/service/api)

        ```java
        public interface PostAPI {
            /**
             * 카테고리 대륙별로 가져오는 메소드
             * @param category
             * @return
             */
            @GET("/post/{category}/list/")
            Observable<Response<PostResult>> getPostList(@Path("category")int category);

            /**
             * Upload Cover
             *
             * @param postCoverMap
             * @return
             */
            @Multipart
            @POST("/post/create/")
            Observable<Response<PostCover>> uploadPostCover(@PartMap Map<String, RequestBody> postCoverMap);

            /**
             * Post Delete
             *
             * @param postPk
             * @return
             */
            @DELETE("/post/{post_pk}/update/")
            Observable<Response<Void>> deletePost( @Path("post_pk") int postPk);

            // 생략...
        }
        ```

        - Retrofit2 생성 Class 생성 [[소스코드]](https://github.com/Hooooong/_Explog/blob/master/app/src/main/java/com/hongsup/explog/service/ServiceGenerator.java)

        ```java
        public class ServiceGenerator {

            private static final String TAG = "ServiceGenerator";

            /**
             * Retrofit2 생성
             *
             * @param className
             * @param <I>
             * @return
             */
            public static <I> I create(Class<I> className) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(SERVER_URL)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                return retrofit.create(className);
            }

            // 새약...
        }
        ```

        - HTTP 통신 사용 (RemoteDataSource에서 사용) [[소스코드]](https://github.com/Hooooong/_Explog/blob/master/app/src/main/java/com/hongsup/explog/data/post/source/PostRemoteDataSource.java)

        ```java
        private PostRemoteDataSource() {
            // Service 생성
            postAPI = ServiceGenerator.create(PostAPI.class);
        }

        @Override
        public Observable<Response<PostContentResult>> getPostContentList(int postPk) {
            return postAPI.getPostContentList(postPk);
        }
        ```

        - Presenter 에서 활용 (RxJava 를 통해 Observable 을 활용한다.)

        ```java
        @Override
        public void loadPostContent(PostCover cover) {
            view.showProgress();

            // Observable 구독
            repository.getPostContentList(postPk)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .filter(Response::isSuccessful)
                    .subscribe(data -> {
                                Log.e(TAG, "loadPostContent: 데이터 로드 완료");
                                view.hideProgress();

                                if (data.body().getPostContentList() == null || data.body().getPostContentList().size() == 0) {
                                    adapterModel.setInit(cover.getLiked(), cover.getLikeCount(), cover.getAuthor());
                                } else {
                                    // Log.e(TAG, "loadPostContent: " + data.body().getPostContentList().toString());
                                    adapterModel.setItems(data.body().getPostContentList());
                                    adapterModel.setLikeAndFollow(cover.getLiked(), cover.getLikeCount(), cover.getAuthor());
                                }
                                adapterView.notifyAllAdapter();
                            },
                            throwable -> {
                                Log.e(TAG, "loadPostContent: 데이터 로드 실패2");
                                view.hideProgress();
                            });
        }        
        ```

    2. File(Image) Upload 방법

        - Retrofit2 API 에 `PartMap` 적용

        ```java
        /**
         * Upload Cover
         *
         * @param postCoverMap
         * @return
         */
        @Multipart
        @POST("/post/create/")
        Observable<Response<PostCover>> uploadPostCover(@PartMap Map<String, RequestBody> postCoverMap);
        ```

        - RemoteDataSource에서 PartMap 활용

        ```java
        @Override
        public Observable<Response<PostCover>> createPostCover(PostCover cover) {

            Map<String, RequestBody> requestBodyMap = new HashMap<>();

            // File 과 함께 보내는 Data 를 RequestBody 로 설정
            requestBodyMap.put("title", toRequestBody(cover.getTitle()));
            // 생략

            /*
             Cover 사진이 있으면 그대로 보내주고,
             없으면 공백을 보내준다.
             */
            if (cover.getCoverPath() != null) {
                File file = new File(cover.getCoverPath());
                // create RequestBody instance from file
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                requestBodyMap.put("img\"; filename=\"" + file.getName(), requestFile);
            } else {
                RequestBody requestFile = RequestBody.create(MediaType.parse(""), "");
                requestBodyMap.put("img\"; filename=\"", requestFile);
            }

            return postTokenAPI.uploadPostCover(requestBodyMap);
        }
        ```

    3. Interceptor 사용

        - HTTP 통신 전, 후로 필수로 송수신 되는 값들(Authorization 등)을 OkHTTP3 를 통해 Custom하게 활용할 수 있다.

        - Interceptor 생성 [[소스코드]](https://github.com/Hooooong/_Explog/blob/master/app/src/main/java/com/hongsup/explog/service/AddTokenInterceptor.java)

        ```java
        public class AddTokenInterceptor implements Interceptor {

            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response;

                Request request = chain.request();
                request = request.newBuilder()
                        .addHeader("Authorization", "Token " + UserRepository.getInstance().getUser().getToken())
                        .build();

                try {
                    response = chain.proceed(request);
                } catch (ProtocolException e) {

                    /**
                     *  Code : 204 일 경우에는
                     *  Response 를 새로 생성하여 클라이언트에 제공
                     *
                     *  ProtocolException : Content-Length 가 0 보다 크기 때문에 Error 가 발생하는 듯...
                     */
                    response = new Response.Builder()
                            .request(chain.request())
                            .code(204)
                            .protocol(Protocol.HTTP_1_1)
                            .addHeader("content-type","application/json")
                            .body(ResponseBody.create(MediaType.parse("text/pain"),""))
                            .message("")
                            .build();
                }
                return response;
            }
        }
        ```

        - Retrofit2 생성 변경[[소스코드]](https://github.com/Hooooong/_Explog/blob/master/app/src/main/java/com/hongsup/explog/service/ServiceGenerator.java#L41)

        ```java
        public static<I> I createInterceptor(Class<I> className){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(SERVER_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    /*
                     Interceptor 추가
                     OkHttpClient 를 Custom하게 생성한다.
                    */
                    .client(new OkHttpClient.Builder().addInterceptor(new AddTokenInterceptor()).build())
                    .build();
            return retrofit.create(className);
        }
        ```

3. RxBinding

    - RxBinding 을 활용하여 TextView 의 변화를 Observable 로 받아 보다 짧은 코드로 사용할 수 있다.[[소스코드]](https://github.com/Hooooong/_Explog/blob/master/app/src/main/java/com/hongsup/explog/view/search/view/SearchView.java#L174)

    ```java
    // 기존 TextWatcher 를 사용한 코드
    editSearch.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.d("확인", "onTextChanged: " + s.toString() + start + before + count);
            if(count>0){
               recyclerSearchHistory.setAdapter(searchRecyclerResultAdapter);
            }else if(start==0){
                recyclerSearchHistory.setAdapter(searchRecyclerAdapter);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    });    

    //RxBinding 을 사용한 코드
    RxTextView.textChanges(editSearch)
                .subscribe(ch ->{
                    if(ch.length()>0){
                        recyclerSearchHistory.setAdapter(searchRecyclerResultAdapter);
                    }else{
                        recyclerSearchHistory.setAdapter(searchRecyclerAdapter);
                    }
                });
    ```

4. SharedPreferences 활용

    - SharedPreferences를 통해 User 정보 활용 및 자동 로그인 적용

    1. `PreferenceUtil` 생성 [[소스코드]](https://github.com/Hooooong/_Explog/blob/master/app/src/main/java/com/hongsup/explog/util/PreferenceUtil.java)

        - SharedPreference 생성과 data 의 get,set, remove 메소드 작성

        ```java
        public class PreferenceUtil {
            private static final String filename = "explog";

            private static SharedPreferences getPreference(Context context){
                return context.getSharedPreferences(filename, Context.MODE_PRIVATE);
            }

            public static void setValue(Context context, String key, String value){
                SharedPreferences.Editor editor = getPreference(context).edit();
                editor.putString(key, value);
                editor.commit();
            }
            public static String getString(Context context, String key){
                return getPreference(context).getString(key,"");
            }

            public static void removeAllValue(Context context){
                SharedPreferences.Editor editor = getPreference(context).edit();
                editor.clear();
                editor.commit();
            }
        }
        ```

    2. SignIn 통신 시 `SharedPreferences` 사용 [[소스코드]](https://github.com/Hooooong/_Explog/blob/master/app/src/main/java/com/hongsup/explog/view/signin/presenter/SignInPresenter.java#L47)

        - 성공 시 SignIn 에 필요한 값을 저장하고, 실패 시 기존에 정보가 있으면 삭제한다.

        ```java
        @Override
        public void getSignIn(SignIn signIn) {
            Observable<Response<User>> observable = repository.signIn(signIn);
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(data -> {
                        if(data.isSuccessful()){
                            /*
                               로그인이 성공했을 경우
                               자동 로그인에 필요한 SharedPreference 에 저장한다.
                             */
                            PreferenceUtil.setValue(context, "Email", signIn.getEmail());
                            PreferenceUtil.setValue(context, "password", signIn.getPassword());
                            PreferenceUtil.setValue(context, "token", data.body().getToken());

                        } else {
                            /*
                              로그인이 실패하였을 경우
                              기존에 있던 SharedPreference 의 정보를 다 지운다.
                            */
                            PreferenceUtil.removeAllValue(context);
                        }
                    });
        }
        ```

    3. SplashActivity 에서 `SharedPreference` 정보를 사용하여 자동 로그인 실행 [[소스코드]](https://github.com/Hooooong/_Explog/blob/master/app/src/main/java/com/hongsup/explog/view/splash/SplashActivity.java#L39)

        ```java
        // 자동 로그인 처리
        if(!TextUtils.isEmpty(PreferenceUtil.getString(SplashActivity.this, "Email"))){
            /**
             * SharedPreference 에 회원정보가 있으면
             * 자동 로그인 처리
             */
            SignIn sign = new SignIn();
            sign.setEmail(PreferenceUtil.getString(SplashActivity.this, "Email"));
            sign.setPassword(PreferenceUtil.getString(SplashActivity.this, "password"));

            splashPresenter.getSignIn(sign);
        }else{
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        ```

5. UI Component

    - ConstraintLayout, CoordinateLayout, RecyclerView 등 다양한 Layout 활용

6. SQLite

    - SQLite를 사용하여 최근 검색어 저장 및 활용

    1. DBHelperUtil 작성[[소스코드]](https://github.com/Hooooong/_Explog/blob/master/app/src/main/java/com/hongsup/explog/util/DBHelperUtil.java)

        - SQLiteOpenHelper 를 상속받아 작성

        ```java
        public class DBHelperUtil extends SQLiteOpenHelper {

            private static final String DB_NAME = "explog.db";
            private static final int DB_VERSION = 1;

            private static DBHelperUtil instance = null;

            public static DBHelperUtil getInstance(Context context) {
                if (instance == null) {
                    instance = new DBHelperUtil(context);
                }
                return instance;
            }

            private DBHelperUtil(Context context) {
                super(context, DB_NAME, null, DB_VERSION);
            }

            @Override
            public void onCreate(SQLiteDatabase db) {
                String createQuery = "CREATE TABLE 'history' (" +
                        " 'id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        " 'word' TEXT " +
                        " )";
                db.execSQL(createQuery);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            }
        }
        ```

    2. SearchLocalDataSource 에서 DBHelperUtil 사용[[소스코드]](https://github.com/Hooooong/_Explog/blob/master/app/src/main/java/com/hongsup/explog/data/search/source/SearchLocalDataSource.java)

        - 최근검색어 List, 검색어 삽입, 삭제 메소드 작성

        ```java
        // 최근검색어 List 를 가져오는 메소드
        @Override
        public List<String> loadRecentSearchWord() {
            String query = " SELECT id, word" +
                           " FROM history";
            List<String> wordList = new ArrayList<>();

            SQLiteDatabase connection = dbHelper.getReadableDatabase();

            Cursor cursor = connection.rawQuery(query, null);
            while (cursor.moveToNext()) {
                String word = cursor.getString(1);
                wordList.add(0, word);
            }
            cursor.close();
            connection.close();
            close();

            return wordList;
        }

        // 최근 검색어 삽입하는 메소드
        @Override
        public void insertSearchWord(String word) {
            String query = " INSERT INTO history(word)" + "" +
                           " values('" + word + "')";
            SQLiteDatabase connection = dbHelper.getReadableDatabase();
            connection.execSQL(query);
            connection.close();
            close();
        }

        // 최근 검색어 삭제하는 메소드
        @Override
        public void deleteSearchWord(String word) {
            String query = " DELETE FROm history " +
                           " WHERE word = '" + word + "'";
            SQLiteDatabase connection = dbHelper.getReadableDatabase();
            connection.execSQL(query);
            connection.close();
            close();
        }
        ```

7. Permmission

    - BaseActivity를 구현하여 권한이 필요한 Activity 에서 상속받아 사용

    1. BaseActivity 추상클래스 생성 [[소스코드]](https://github.com/Hooooong/_Explog/blob/master/app/src/main/java/com/hongsup/explog/view/base/BaseActivity.java)

        - `init()` 메소드를 추상메소드로 작성하여 권한이 승인되면 구현한 init 메소드를 실행

        ```java
        public abstract class BaseActivity extends AppCompatActivity {

            private static final int REQ_CODE  = 999;
            private String permissions[] ;

            public abstract void init();

            public BaseActivity(String[] permissions) {
                this.permissions = permissions;
            }

            @Override
            protected void onCreate(@Nullable Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    checkPermission();
                }else{
                    init();
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            public void checkPermission(){
                List<String> requires = new ArrayList<>();
                for(String permission : permissions){
                    if(checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED){
                        requires.add(permission);
                    }
                }

                if(requires.size() > 0){
                    String perm[] = requires.toArray(new String[requires.size()]);
                    requestPermissions(perm, REQ_CODE);
                }else{
                    init();
                }
            }

            @Override
            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                switch (requestCode){
                    case REQ_CODE:
                        boolean flag = true;
                        for(int grantResult : grantResults){
                            if(grantResult != PackageManager.PERMISSION_GRANTED){
                                flag = false;
                                break;
                            }
                        }
                        if(flag){
                            init();
                        }else{
                            Toast.makeText(this, "권한 승인을 하지 않으면 APP 을 사용할 수 없습니다.", Toast.LENGTH_LONG).show();
                            finish();
                        }
                        break;
                }
            }
        }
        ```

    2. 권한이 필요한 기능에서 BaseActivity 상속받아 사용 [[소스코드]](https://github.com/Hooooong/_Explog/blob/master/app/src/main/java/com/hongsup/explog/view/gallery/GalleryActivity.java#L47)

        ```java
        public class GalleryActivity extends BaseActivity  {

            private static final String TAG = "GalleryActivity";

            public GalleryActivity() {
                // 필요한 권한을 부모 클래스의 생성자로 넘겨준다.
                super(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
            }

            // 권한이 승인되면 init() 메소드 호출된다.
            @Override
            public void init() {
                setContentView(R.layout.activity_gallery);
                initLayout();
                initGalleryAdapter();
                setGallery();
            }

            // 생략...
        }
        ```

## 소스 코드

  - [전체 소스코드](https://github.com/Hooooong/_Explog/tree/master/app/src/main/java/com/hongsup/explog)
