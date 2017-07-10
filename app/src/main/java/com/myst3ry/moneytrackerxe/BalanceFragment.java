package com.myst3ry.moneytrackerxe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.myst3ry.moneytrackerxe.api.BalanceResult;

public class BalanceFragment extends Fragment {

    private static final int LOADER_BALANCE = 4;

    private TextView totalBalance;
    private TextView totalExpenses;
    private TextView totalIncomes;
    private DiagramView diagramView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_balance, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        totalBalance = (TextView) view.findViewById(R.id.total_balance);
        totalExpenses = (TextView) view.findViewById(R.id.total_expenses);
        totalIncomes = (TextView) view.findViewById(R.id.total_incomes);
        diagramView = (DiagramView) view.findViewById(R.id.diagram_view);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            updateData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateData();
    }

    public void updateData() {
        getLoaderManager().restartLoader(LOADER_BALANCE, null, new LoaderManager.LoaderCallbacks<BalanceResult>() {

            @Override
            public Loader<BalanceResult> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<BalanceResult>(getContext()) {
                    @Override
                    public BalanceResult loadInBackground() {
                        try {
                            return ((LSApp) getActivity().getApplication()).getApi().balance().execute().body();
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<BalanceResult> loader, BalanceResult data) {
                if (data != null && data.isSuccessful()) {
                    totalBalance.setText(getString(R.string.rub_sign_format, data.getTotalIncomes() - data.getTotalExpenses()));
                    totalExpenses.setText(getString(R.string.rub_sign_format, data.getTotalExpenses()));
                    totalIncomes.setText(getString(R.string.rub_sign_format, data.getTotalIncomes()));
                    diagramView.update(data.getTotalExpenses(), data.getTotalIncomes());
                } else {
                    Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onLoaderReset(Loader<BalanceResult> loader) {
            }
        }).forceLoad();
    }
}