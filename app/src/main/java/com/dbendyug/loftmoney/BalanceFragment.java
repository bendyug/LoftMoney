package com.dbendyug.loftmoney;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BalanceFragment extends Fragment {

    private DiagramView diagramView;
    private LoftApi loftApi;
    private TextView totalBalance;
    private TextView totalExpense;
    private TextView totalIncome;

    public static BalanceFragment newInstance() {
        return new BalanceFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_balance, container, false);

        diagramView = fragmentView.findViewById(R.id.diagram_view);
        totalBalance = fragmentView.findViewById(R.id.total_balance);
        totalExpense = fragmentView.findViewById(R.id.total_expense);
        totalIncome = fragmentView.findViewById(R.id.total_income);

        return fragmentView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loftApi = ((LoftApp) Objects.requireNonNull(getActivity()).getApplication()).getLoftApi();
    }

    @Override
    public void onStart() {
        super.onStart();

        loadBalance();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            loadBalance();
        }
    }

    public void loadBalance() {
        SharedPreferences sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString(MainActivity.AUTH_TOKEN, "");
        Call<BalanceResponse> balanceResponseCall = loftApi.getBalance(authToken);
        balanceResponseCall.enqueue(new Callback<BalanceResponse>() {
            @Override
            public void onResponse(Call<BalanceResponse> call, Response<BalanceResponse> response) {
                totalBalance.setText(getString(R.string.price_template_no_spacebar, String.valueOf(response.body().getTotalIncome() - response.body().getTotalExpense())));
                totalExpense.setText(getString(R.string.price_template_no_spacebar, String.valueOf(response.body().getTotalExpense())));
                totalIncome.setText(getString(R.string.price_template_no_spacebar, String.valueOf(response.body().getTotalIncome())));

                diagramView.updateMoney(response.body().getTotalExpense(), response.body().getTotalIncome());
            }

            @Override
            public void onFailure(Call<BalanceResponse> call, Throwable t) {

            }
        });
    }


}
